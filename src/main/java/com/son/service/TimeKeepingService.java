package com.son.service;

import com.son.constant.Exceptions;
import com.son.dto.TimeKeepingView;
import com.son.entity.Personnel;
import com.son.entity.Requests;
import com.son.entity.TimeKeeping;
import com.son.handler.ApiException;
import com.son.repository.TimeKeepingRepository;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.util.common.DateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public TimeKeeping findOneTimeKeeping(
            Credentials credentials, Integer timeKeepingId
    ) throws ApiException {
        // get current timekeeping
        Optional<TimeKeeping> opTimeKeeping = timeKeepingRepository.findById(timeKeepingId);
        if (!opTimeKeeping.isPresent()) {
            throw new ApiException(400, Exceptions.TIME_KEEPING_NOT_FOUND);
        }

        return opTimeKeeping.get();
    }

    public Boolean deleteTimeKeeping(
            Credentials credentials, Integer personnelId, Integer timeKeepingId
    ) throws ApiException {
        // validate personnel, timekeeping
        personnelService.findOne(personnelId);

        TimeKeeping timeKeeping = findOneTimeKeeping(credentials, timeKeepingId);

        timeKeepingRepository.delete(timeKeeping);

        return true;
    }

    public TimeKeeping updateTimeKeeping(
            Credentials credentials, Integer personnelId, Integer timeKeepingId,
            UpdateTimeKeepingRequest timeKeepingRequest
    ) throws ApiException {
        // validate personnel, request
        personnelService.findOne(personnelId);

        Requests request = null;
        if (timeKeepingRequest.getRequestId() != null) {
            request = requestsService.findOne(timeKeepingRequest.getRequestId());
        }

        TimeKeeping timeKeeping = findOneTimeKeeping(credentials, timeKeepingId);
        modelMapper.map(timeKeepingRequest, timeKeeping);

        timeKeeping.setRequest(request);

        return timeKeepingRepository.save(timeKeeping);
    }

    public List<TimeKeepingView> findTimeKeeping(
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
                throw new ApiException(400, "Error date format");
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


    public ByteArrayInputStream exportTimeKeeping(
            Credentials credentials, FindAllTimeKeepingExcelRequest request
    ) throws ApiException {
        try {
            FindAllTimeKeepingRequest findAll = modelMapper.map(request, FindAllTimeKeepingRequest.class);
            List<TimeKeepingView> timeKeepingViewList = findTimeKeeping(credentials, findAll);

            String SHEET = "Bảng chấm công";
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Sheet sheet = workbook.createSheet(SHEET);

            List<String> dateStringList = request.getDates();
            List<Date> dateList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (String dateString : dateStringList) {
                try {
                    dateList.add(sdf.parse(dateString));
                } catch (ParseException e) {
                    throw new ApiException(400, "Error date format");
                }
            }

            final String COLUMN_ORDINAL_NUMBER = "STT";
            final String COLUMN_FULL_NAME = "Họ tên";
            final String COLUMN_DEPARTMENT = "Phòng ban";
            final String COLUMN_POSITION = "Vị trí";

            List<String> personnelColumns = Arrays.asList(
                    COLUMN_ORDINAL_NUMBER, COLUMN_FULL_NAME, COLUMN_DEPARTMENT, COLUMN_POSITION
            );

            // TITLE
            int rowIdx = 0;

            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.setHeight((short) -1);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("  Bảng chấm công");
            CellStyle css = workbook.createCellStyle();
            setTitleCellStyle(workbook, css);
            setCommonCellStyle(css);
            titleCell.setCellStyle(css);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,
                    personnelColumns.size() + dateList.size() - 1));

            // HEADER PERSONNEL
            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < personnelColumns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(personnelColumns.get(i));

                CellStyle cs = workbook.createCellStyle();
                cell.setCellStyle(cs);

                setCommonCellStyle(cs);
                setTitleColumnCellStyle(workbook, cs);

                sheet.autoSizeColumn(i);
            }
            // HEADER TIMEKEEPING
            for (int i = 0; i < dateList.size(); i++) {
                Date date = dateList.get(i);

                Cell cell = headerRow.createCell(personnelColumns.size() + i);
                cell.setCellValue(DateUtil.getDateForTitle(date));

                CellStyle cs = workbook.createCellStyle();
                cell.setCellStyle(cs);

                setCommonCellStyle(cs);
                setTitleColumnCellStyle(workbook, cs);

                if (DateUtil.getDayOfWeek(date) == Calendar.SUNDAY) {
                    setBackGroundCellStyle(cs);
                }

                sheet.autoSizeColumn(i);
            }

            // ROWS
            int n = dateList.size();
            for (TimeKeepingView view : timeKeepingViewList) {
                Row row = sheet.createRow(rowIdx++);
                List<TimeKeeping> timeKeepingList = view.getTimeKeepingList();
                Personnel personnel = view.getPersonnel();
                // PERSONNEL
                for (int j = 0; j < personnelColumns.size(); j++) {
                    String value = "";
                    Cell cell = row.createCell(j);

                    CellStyle cs = workbook.createCellStyle();
                    cell.setCellStyle(cs);
                    setCommonCellStyle(cs);

                    switch (personnelColumns.get(j)) {
                        case COLUMN_ORDINAL_NUMBER:
                            value = (rowIdx - 2) + "";
                            break;
                        case COLUMN_FULL_NAME:
                            value = personnel.getFullName();
                            break;
                        case COLUMN_DEPARTMENT:
                            if (personnel.getDepartment() != null)
                                value = personnel.getDepartment().getName();
                            break;
                        case COLUMN_POSITION:
                            value = personnel.getPosition();
                            break;
                        default:
                            value = "";
                    }

                    cell.setCellValue(value);
                }

                // TIMEKEEPING
                for (int j = 0; j < n; j++) {
                    Date date = dateList.get(j);
                    String value = null;

                    TimeKeeping timeKeeping = timeKeepingList.get(j);
                    if (timeKeeping != null) {
                        value = timeKeeping.getStatus();
                    }

                    if (timeKeeping == null) {
                        value = "0";
                    }

                    Cell cell = row.createCell(j + personnelColumns.size());
                    cell.setCellValue(value == null ? "" : value);

                    CellStyle cs = workbook.createCellStyle();
                    cell.setCellStyle(cs);
                    setCommonCellStyle(cs);

                    if (DateUtil.getDayOfWeek(date) == Calendar.SUNDAY) {
                        setBackGroundCellStyle(cs);
                    }
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new ApiException(500, "Fail to export data");
        }
    }

    public void setCommonCellStyle(CellStyle cs) {
        cs.setWrapText(true);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBottomBorderColor(IndexedColors.BLACK.index);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setLeftBorderColor(IndexedColors.BLACK.index);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setRightBorderColor(IndexedColors.BLACK.index);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setTopBorderColor(IndexedColors.BLACK.index);
    }

    public void setBackGroundCellStyle(CellStyle cs) {
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setFillForegroundColor(IndexedColors.YELLOW.index);
    }

    public void setTitleCellStyle(Workbook workbook, CellStyle cs) {
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 20);
        cs.setFont(titleFont);
    }

    public void setTitleColumnCellStyle(Workbook workbook, CellStyle cs) {
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 11);
        cs.setFont(titleFont);
    }

}
