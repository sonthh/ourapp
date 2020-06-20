package com.son.service;

import com.son.dto.TimeKeepingView;
import com.son.entity.Personnel;
import com.son.entity.Requests;
import com.son.entity.TimeKeeping;
import com.son.handler.ApiException;
import com.son.repository.TimeKeepingRepository;
import com.son.request.DoTimeKeepingRequest;
import com.son.request.FindAllPersonnelRequest;
import com.son.request.FindAllTimeKeepingRequest;
import com.son.security.Credentials;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeKeepingService {
    private final RequestsService requestsService;
    private final UserService userService;
    private final PersonnelService personnelService;
    @PersistenceContext
    private EntityManager entityManager;

    private final ModelMapper modelMapper;
    private final TimeKeepingRepository timeKeepingRepository;

    public TimeKeeping doTimeKeeping(
            Credentials credentials, Integer personnelId,
            DoTimeKeepingRequest timeKeepingRequest
    ) throws ApiException {
        Personnel personnel = personnelService.findOne(personnelId);
        Requests request = null;
        if (timeKeepingRequest.getRequestId() != null) {
            request = requestsService.findOne(timeKeepingRequest.getRequestId());
        }

        TimeKeeping timeKeeping = modelMapper.map(timeKeepingRequest, TimeKeeping.class);
        timeKeeping.setPersonnel(personnel);
        timeKeeping.setRequest(request);

        return timeKeepingRepository.save(timeKeeping);
    }

    public Object findTimeKeeping(
            Credentials credentials, FindAllTimeKeepingRequest request
    ) throws ApiException {
        // validate input
        List<String> dateStringList = request.getDates();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Date> dateList = new ArrayList<>();
        for (String dateString : dateStringList) {
            try {
                dateList.add(sdf.parse(dateString));
            } catch (ParseException e) {
                return new ApiException(400, "Date format");
            }
        }

        // find personnel
        FindAllPersonnelRequest findAll = modelMapper.map(request, FindAllPersonnelRequest.class);
        findAll.setLimit(Integer.MAX_VALUE);
        Page<Personnel> page = personnelService.findMany(credentials, findAll);
        List<Personnel> personnelList = page.getContent();

        // list of personnel id
        List<Integer> personnelIds = personnelList.stream().map(Personnel::getId).collect(Collectors.toList());

        // find list of timekeeping
        String hql = "SELECT tk FROM TimeKeeping tk " +
                "WHERE tk.personnel.id IN :personnelIds AND tk.date IN :dates " +
                "ORDER BY tk.personnel.id DESC, tk.date ASC";

        List<TimeKeeping> keepingList = entityManager.createQuery(hql, TimeKeeping.class)
                .setParameter("personnelIds", personnelIds)
                .setParameter("dates", dateList)
                .getResultList();

        // separate list of timekeeping to multiple list by personnelId
        Map<Integer, List<TimeKeeping>> subs = new HashMap<>();

        for (TimeKeeping each : keepingList) {
            List<TimeKeeping> temp = subs.get(each.getPersonnel().getId());

            if (temp == null) {
                temp = new ArrayList<>();
                subs.put(each.getPersonnel().getId(), temp);
            }

            temp.add(each);
        }

        List<TimeKeepingView> result = new ArrayList<>();
        for (Personnel personnel : personnelList) {
            List<TimeKeeping> timeKeepingList = subs.get(personnel.getId());

            if (timeKeepingList == null) {
                timeKeepingList = new ArrayList<>();
            }

            int n = dateList.size();
            for (int i = 0; i < n; i++) {
                Date date = dateList.get(i);

                boolean needAddNull = i >= timeKeepingList.size()
                        || !date.equals(timeKeepingList.get(i).getDate());

                if (needAddNull) {
                    timeKeepingList.add(i, null);
                }
            }

            result.add(new TimeKeepingView(personnel, timeKeepingList));
        }
        return result;
    }

}
