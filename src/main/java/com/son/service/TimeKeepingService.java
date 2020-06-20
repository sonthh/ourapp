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
        FindAllPersonnelRequest findAll = modelMapper.map(request, FindAllPersonnelRequest.class);
        findAll.setLimit(Integer.MAX_VALUE);
        Page<Personnel> page = personnelService.findMany(credentials, findAll);
        List<Personnel> personnelList = page.getContent();
        List<Integer> ids = personnelList.stream().map(Personnel::getId).collect(Collectors.toList());
        List<String> dates = request.getDates();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Date> dateList = new ArrayList<>();
        for (String dateString : dates) {
            try {
                dateList.add(sdf.parse(dateString));
            } catch (ParseException e) {
                return new ApiException(400, "Date format");
            }
        }

        String hql = "SELECT tk FROM TimeKeeping tk " +
                "WHERE tk.personnel.id IN :personnelIds AND tk.date IN :dates " +
                "ORDER BY tk.personnel.id DESC, tk.date ASC";
        List<TimeKeeping> x = entityManager.createQuery(hql, TimeKeeping.class)
                .setParameter("personnelIds", ids)
                .setParameter("dates", dateList)
                .getResultList();

        // create the thing to store the sub lists
        Map<Integer, List<TimeKeeping>> subs = new HashMap<>();

        // iterate through your objects
        for (TimeKeeping o : x) {

            // fetch the list for this object's id
            List<TimeKeeping> temp = subs.get(o.getPersonnel().getId());

            if (temp == null) {
                // if the list is null we haven't seen an
                // object with this id before, so create
                // a new list
                temp = new ArrayList<>();

                // and add it to the map
                subs.put(o.getPersonnel().getId(), temp);
            }

            // whether we got the list from the map
            // or made a new one we need to add our
            // object.
            temp.add(o);
        }

        List<TimeKeepingView> result = new ArrayList<>();
        for (Personnel personnel : personnelList) {
            List<TimeKeeping> timeKeepingList = subs.get(personnel.getId());
            result.add(new TimeKeepingView(personnel, timeKeepingList));
        }

        return result;
    }

}
