package com.son.service;

import com.son.constant.Exceptions;
import com.son.dto.SalaryView;
import com.son.entity.*;
import com.son.handler.ApiException;
import com.son.model.Gender;
import com.son.repository.*;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.util.common.EnumUtil;
import com.son.util.page.PageUtil;
import com.son.util.spec.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.son.util.spec.SearchOperation.*;

@Service
@RequiredArgsConstructor
@Setter
public class PersonnelService {
    private final PersonnelRepository personnelRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final UserRepository userRepository;
    private final IdentificationRepository identificationRepository;
    private final PassportRepository passportRepository;
    private final WorkingTimeRepository workingTimeRepository;
    private final AdditionalInfoRepository additionalInfoRepository;
    private final HealthyStatusRepository healthyStatusRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final QualificationRepository qualificationRepository;
    private final ModelMapper modelMapper;
    private final WorkHistoryRepository workHistoryRepository;
    private final CertificationRepository certificationRepository;
    private final BankInfoRepository bankInfoRepository;
    private final CloudinaryService cloudinaryService;
    private final RequestsService requestsService;
    private final SalaryRepository salaryRepository;
    private final AllowancesRepository allowancesRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Personnel findOne(Integer personnelId) throws ApiException {
        Optional<Personnel> optional = personnelRepository.findById(personnelId);
        if (!optional.isPresent()) {
            throw new ApiException(404, Exceptions.PERSONNEL_NOT_FOUND);
        }

        return optional.get();
    }

    public Boolean isDeletedOne(int personnelId) throws ApiException {
        Optional<Personnel> personnel = personnelRepository.findById(personnelId);
        if (!personnel.isPresent()) {
            throw new ApiException(404, Exceptions.PERSONNEL_NOT_FOUND);
        }
        personnelRepository.deleteById(personnelId);
        return true;
    }

    public Boolean deteleMany(DeleteManyByIdRequest deleteManyByIdRequest) throws ApiException {
        List<Integer> ids = deleteManyByIdRequest.getIds();
        List<Personnel> personnels = (List<Personnel>) personnelRepository.findAllById(ids);

        if (personnels.size() != ids.size()) {
            throw new ApiException(404, Exceptions.PERSONNEL_NOT_FOUND);
        }

        personnelRepository.deleteAll(personnels);

        return true;
    }

    // create one personnel with basic information
    public Personnel createOne(CreatePersonnelRequest createPersonnelRequest) throws ApiException {
        Optional<Department> department = departmentRepository.findById(createPersonnelRequest.getDepartmentId());

        if (!department.isPresent()) {
            throw new ApiException(404, Exceptions.DEPARTMENT_NOT_FOUND);
        }

        Personnel newPersonnel = modelMapper.map(createPersonnelRequest, Personnel.class);

        newPersonnel.setDepartment(department.get());

        return personnelRepository.save(newPersonnel);
    }

    public Personnel updateBasicInfo(
            UpdatePersonnelBasicInfo personnelRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Integer departmentId = personnelRequest.getDepartmentId();
        if (departmentId != null) {
            Department department = departmentService.findOne(credentials, departmentId);
            personnel.setDepartment(department);
        }

        modelMapper.map(personnelRequest, personnel);

        return personnelRepository.save(personnel);
    }

    public Boolean addIdentification(
            AddIdentificationRequest addIdentification, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getIdentification() != null) {
            throw new ApiException(400, Exceptions.IDENTIFICATION_EXISTED);
        }

        Identification identification = modelMapper.map(addIdentification, Identification.class);
        identification = identificationRepository.save(identification);

        personnel.setIdentification(identification);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateIdentification(
            UpdateIdentificationRequest updateIdentification, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getIdentification() == null) {
            throw new ApiException(400, Exceptions.IDENTIFICATION_NOT_FOUND);
        }

        Identification identification = personnel.getIdentification();
        modelMapper.map(updateIdentification, identification);

        identificationRepository.save(identification);
        return true;
    }

    public Boolean addPassport(
            AddPassportRequest passportRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getPassport() != null) {
            throw new ApiException(400, Exceptions.PASSPORT_EXISTED);
        }

        Passport passport = modelMapper.map(passportRequest, Passport.class);
        passport = passportRepository.save(passport);

        personnel.setPassport(passport);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updatePassport(
            UpdatePassportRequest passportRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getPassport() == null) {
            throw new ApiException(400, Exceptions.PASSPORT_NOT_FOUND);
        }

        Passport passport = personnel.getPassport();
        modelMapper.map(passportRequest, passport);

        passportRepository.save(passport);
        return true;
    }

    public Boolean addWorkingTime(
            AddWorkingTimeRequest workingTimeRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getWorkingTime() != null) {
            throw new ApiException(400, Exceptions.WORKING_TIME_EXISTED);
        }

        WorkingTime workingTime = modelMapper.map(workingTimeRequest, WorkingTime.class);
        workingTime = workingTimeRepository.save(workingTime);

        personnel.setWorkingTime(workingTime);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateWorkingTime(
            UpdateWorkingTimeRequest workingTimeRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getWorkingTime() == null) {
            throw new ApiException(400, Exceptions.WORKING_TIME_NOT_FOUND);
        }

        WorkingTime workingTime = personnel.getWorkingTime();
        modelMapper.map(workingTimeRequest, workingTime);

        workingTimeRepository.save(workingTime);
        return true;
    }

    public Boolean addQualification(
            AddQualificationRequest qualificationRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Qualification qualification = modelMapper.map(qualificationRequest, Qualification.class);
        qualification = qualificationRepository.save(qualification);

        personnel.getQualifications().add(qualification);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateQualification(
            UpdateQualificationRequest qualificationRequest, int personnelId, int qualificationId,
            Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Optional<Qualification> opQualification = qualificationRepository.findById(qualificationId);
        if (!opQualification.isPresent()) {
            throw new ApiException(400, Exceptions.QUALIFICATION_NOT_FOUND);
        }
        Qualification qualification = opQualification.get();

        List<Qualification> qualifications = personnel.getQualifications();
        if (!qualifications.contains(qualification)) {
            throw new ApiException(400, Exceptions.QUALIFICATION_NOT_FOUND);
        }

        modelMapper.map(qualificationRequest, qualification);
        qualificationRepository.save(qualification);
        return true;
    }

    public Boolean deleteQualification(
            int personnelId, int qualificationId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Optional<Qualification> opQualification = qualificationRepository.findById(qualificationId);
        if (!opQualification.isPresent()) {
            throw new ApiException(400, Exceptions.QUALIFICATION_NOT_FOUND);
        }
        Qualification qualification = opQualification.get();

        List<Qualification> qualifications = personnel.getQualifications();
        if (!qualifications.contains(qualification)) {
            throw new ApiException(400, Exceptions.QUALIFICATION_NOT_FOUND);
        }

        qualificationRepository.delete(qualification);
        return true;
    }

    public Page<Personnel> findMany(Credentials credentials, FindAllPersonnelRequest findAllPersonnelRequest)
            throws ApiException {
        Gender gender = EnumUtil.getEnum(Gender.class, findAllPersonnelRequest.getGender());

        String createdBy = findAllPersonnelRequest.getCreatedBy();
        String lastModifiedBy = findAllPersonnelRequest.getLastModifiedBy();
        String fullName = findAllPersonnelRequest.getFullName();
        String email = findAllPersonnelRequest.getEmail();
        String position = findAllPersonnelRequest.getPosition();
        Integer departmentId = findAllPersonnelRequest.getDepartmentId();
        Boolean isStopWork = findAllPersonnelRequest.getIsStopWork();
        List<Integer> userIds = findAllPersonnelRequest.getIds();

        Integer currentPage = findAllPersonnelRequest.getCurrentPage();
        Integer limit = findAllPersonnelRequest.getLimit();
        String sortDirection = findAllPersonnelRequest.getSortDirection();
        String sortBy = findAllPersonnelRequest.getSortBy();


        SpecificationBuilder<Personnel> builder = new SpecificationBuilder<>();
        builder
                .query("position", CONTAINS, position)
                .query("email", CONTAINS, email)
                .query("fullName", CONTAINS, fullName)
                .query("gender", EQUALITY, gender)
                .query("id", IN, userIds)
                .query("department.id", EQUALITY, departmentId)
                .query("createdBy.username", CONTAINS, createdBy)
                .query("lastModifiedBy.username", CONTAINS, lastModifiedBy);

        if (isStopWork != null && isStopWork) {
            builder.query("workingTime.isStopWork", EQUALITY, true);
        }

        Specification<Personnel> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return personnelRepository.findAll(spec, pageable);
    }


    /*====================================WORK HISTORY START==========================================================*/
    public Personnel createWorkHistory(AddWorkHistoryRequest historyRequests, Integer personnelId)
            throws ApiException {
        Personnel personnel = findOne(personnelId);

        WorkHistory workHistory = modelMapper.map(historyRequests, WorkHistory.class);
        workHistory = workHistoryRepository.save(workHistory);

        personnel.getWorkHistories().add(workHistory);

        return personnelRepository.save(personnel);
    }

    public Boolean updateWorkHistory(UpdateWorkHistoryRequest historyRequests,
                                     Integer personnelId, Integer workHistoryId)
            throws ApiException {
        Personnel personnel = findOne(personnelId);
        Optional<WorkHistory> findWorkHistory = workHistoryRepository.findById(workHistoryId);

        if (!findWorkHistory.isPresent()) {
            throw new ApiException(400, Exceptions.WORK_HISTORY_NOT_FOUND);
        }

        WorkHistory workHistory = findWorkHistory.get();

        if (!personnel.getWorkHistories().contains(workHistory)) {
            throw new ApiException(400, Exceptions.WORK_HISTORY_NOT_FOUND);
        }

        modelMapper.map(historyRequests, workHistory);
        workHistoryRepository.save(workHistory);

        return true;
    }

    public Boolean deleteWorkHistory(Integer personnelId, Integer workHistoryId) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Optional<WorkHistory> findWorkHistory = workHistoryRepository.findById(workHistoryId);

        if (!findWorkHistory.isPresent()) {
            throw new ApiException(400, Exceptions.WORK_HISTORY_NOT_FOUND);
        }

        WorkHistory workHistory = findWorkHistory.get();
        List<WorkHistory> list = personnel.getWorkHistories();

        if (!list.contains(workHistory)) {
            throw new ApiException(400, Exceptions.WORK_HISTORY_NOT_FOUND);
        }

        workHistoryRepository.delete(workHistory);

        return true;
    }
    /*====================================WORK HISTORY END============================================================*/


    /*====================================CERTIFICATION START=========================================================*/
    public Personnel createCertification(AddCertificationRequest certificationRequest, Integer personnelId)
            throws ApiException {
        Personnel personnel = findOne(personnelId);

        Certification certification = modelMapper.map(certificationRequest, Certification.class);
        certification = certificationRepository.save(certification);

        personnel.getCertifications().add(certification);

        return personnelRepository.save(personnel);
    }

    public Boolean updateCertification(UpdateCertificationRequest certificationRequest,
                                       Integer personnelId, Integer certificationId) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Optional<Certification> findCertification = certificationRepository.findById(certificationId);
        if (!findCertification.isPresent()) {
            throw new ApiException(400, Exceptions.CERTIFICATION_NOT_FOUND);
        }
        Certification certification = findCertification.get();

        if (!personnel.getCertifications().contains(certification)) {
            throw new ApiException(400, Exceptions.CERTIFICATION_NOT_FOUND);
        }
        modelMapper.map(certificationRequest, certification);
        certificationRepository.save(certification);

        return true;
    }

    public Boolean deleteCertification(Integer personnelId, Integer certificationId) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Optional<Certification> findCertification = certificationRepository.findById(certificationId);
        if (!findCertification.isPresent()) {
            throw new ApiException(400, Exceptions.CERTIFICATION_NOT_FOUND);
        }

        Certification certification = findCertification.get();
        if (!personnel.getCertifications().contains(certification)) {
            throw new ApiException(400, Exceptions.CERTIFICATION_NOT_FOUND);
        }

        certificationRepository.delete(certification);

        return true;
    }


    /*====================================CERTIFICATION END===========================================================*/

    /*====================================ADDITIONAL INFO START=======================================================*/
    public Boolean addAdditionalInfo(
            @Valid AddAdditionalInfoRequest addAdditionalInfoRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getAdditionalInfo() != null) {
            throw new ApiException(400, Exceptions.ADDITIONAL_INFO_EXISTED);
        }

        AdditionalInfo additionalInfo = modelMapper.map(addAdditionalInfoRequest, AdditionalInfo.class);
        additionalInfo = additionalInfoRepository.save(additionalInfo);

        personnel.setAdditionalInfo(additionalInfo);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateAdditionalInfo(
            UpdateAdditionalInfoRequest updateAdditionalInfoRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getAdditionalInfo() == null) {
            throw new ApiException(400, Exceptions.ADDITIONAL_INFO_NOT_FOUND);
        }

        AdditionalInfo additionalInfo = personnel.getAdditionalInfo();
        modelMapper.map(updateAdditionalInfoRequest, additionalInfo);

        additionalInfoRepository.save(additionalInfo);
        return true;
    }
    /*====================================ADDITIONAL INFO END=========================================================*/

    /*====================================HEALTHY STATUS START========================================================*/
    public Boolean addHealthyStatus(
            @Valid AddHealthyStatusRequest addHealthyStatusRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getHealthyStatus() != null) {
            throw new ApiException(400, Exceptions.HEALTHY_STATUS_EXISTED);
        }

        HealthyStatus healthyStatus = modelMapper.map(addHealthyStatusRequest, HealthyStatus.class);
        healthyStatus = healthyStatusRepository.save(healthyStatus);

        personnel.setHealthyStatus(healthyStatus);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateHealthyStatus(
            UpdateHealthyStatusRequest updateHealthyStatusRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getHealthyStatus() == null) {
            throw new ApiException(400, Exceptions.HEALTHY_STATUS_NOT_FOUND);
        }

        HealthyStatus healthyStatus = personnel.getHealthyStatus();
        modelMapper.map(updateHealthyStatusRequest, healthyStatus);

        healthyStatusRepository.save(healthyStatus);
        return true;
    }
    /*====================================HEALTHY STATUS END==========================================================*/

    /*====================================CONTACT INFO START==========================================================*/
    public Boolean addContactInfo(
            @Valid AddContactInfoRequest addContactInfoRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getContactInfo() != null) {
            throw new ApiException(400, Exceptions.CONTACT_INFO_EXISTED);
        }

        ContactInfo contactInfo = modelMapper.map(addContactInfoRequest, ContactInfo.class);
        contactInfo = contactInfoRepository.save(contactInfo);

        personnel.setContactInfo(contactInfo);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateContactInfo(
            UpdateContactInfoRequest updateContactInfoRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getContactInfo() == null) {
            throw new ApiException(400, Exceptions.CONTACT_INFO_NOT_FOUND);
        }

        ContactInfo contactInfo = personnel.getContactInfo();
        modelMapper.map(updateContactInfoRequest, contactInfo);

        contactInfoRepository.save(contactInfo);
        return true;
    }
    /*====================================CONTACT INFO END============================================================*/

    /*====================================BANK INFO END===============================================================*/
    public Boolean addBankInfo(
            @Valid AddBankInfoRequest addBankInfoRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getBankInfo() != null) {
            throw new ApiException(400, Exceptions.BANK_INFO_EXISTED);
        }

        BankInfo bankInfo = modelMapper.map(addBankInfoRequest, BankInfo.class);
        bankInfo = bankInfoRepository.save(bankInfo);

        personnel.setBankInfo(bankInfo);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateBankInfo(
            UpdateBankInfoRequest updateBankInfoRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getBankInfo() == null) {
            throw new ApiException(400, Exceptions.BANK_INFO_NOT_FOUND);
        }

        BankInfo bankInfo = personnel.getBankInfo();
        modelMapper.map(updateBankInfoRequest, bankInfo);

        bankInfoRepository.save(bankInfo);
        return true;
    }

    /*====================================BANK INFO END===============================================================*/
    public ByteArrayInputStream exportPersonnel() {
        String[] HEADERs = {"Id", "Title", "Description", "Published"};
        String SHEET = "Danh sách nhân viên";

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (int i = 0; i < 10; i++) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue("personnel" + i);
                row.createCell(1).setCellValue("personnel" + i);
                row.createCell(2).setCellValue("personnel" + i);
                row.createCell(3).setCellValue("personnel" + i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public String updateAvatar(
            Integer personnelId, UpdateAvatarRequest updateAvatarRequest, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        String avatar;
        try {
            avatar = cloudinaryService.upload(updateAvatarRequest.getAvatar());
        } catch (IOException e) {
            throw new ApiException(400, Exceptions.UPLOAD_FAILURE);
        }

        if (avatar == null) {
            throw new ApiException(400, Exceptions.UPLOAD_FAILURE);
        }

        personnel.setAvatar(avatar);
        personnelRepository.save(personnel);

        return avatar;
    }

    /*====================================BANK INFO END===============================================================*/
    public Boolean addSalary(
            @Valid AddSalaryRequest addSalaryRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getSalary() != null) {
            throw new ApiException(400, Exceptions.SALARY_EXISTED);
        }

        Salary salary = modelMapper.map(addSalaryRequest, Salary.class);
        salary = salaryRepository.save(salary);

        personnel.setSalary(salary);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateSalary(
            UpdateSalaryRequest updateSalaryRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getSalary() == null) {
            throw new ApiException(400, Exceptions.SALARY_NOT_FOUND);
        }

        Salary salary = personnel.getSalary();
        modelMapper.map(updateSalaryRequest, salary);

        salaryRepository.save(salary);
        return true;
    }
    /*====================================BANK INFO END===============================================================*/

    /*====================================ALLOWANCES END==============================================================*/
    public Boolean addAllowances(
            AddAllowancesRequest addAllowancesRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Allowance allowance = modelMapper.map(addAllowancesRequest, Allowance.class);
        allowance = allowancesRepository.save(allowance);

        personnel.getAllowances().add(allowance);
        personnelRepository.save(personnel);

        return true;
    }

    public Boolean updateAllowances(
            UpdateAllowancesRequest updateAllowancesRequest, int personnelId, int allowanceId,
            Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Optional<Allowance> opAllowances = allowancesRepository.findById(allowanceId);
        if (!opAllowances.isPresent()) {
            throw new ApiException(400, Exceptions.ALLOWANCES_NOT_FOUND);
        }
        Allowance allowance = opAllowances.get();

        List<Allowance> allowances = personnel.getAllowances();
        if (!allowances.contains(allowance)) {
            throw new ApiException(400, Exceptions.ALLOWANCES_NOT_FOUND);
        }

        modelMapper.map(updateAllowancesRequest, allowance);
        allowancesRepository.save(allowance);
        return true;
    }

    public Boolean deleteAllowances(
            int personnelId, int allowancesId, Credentials credentials
    ) throws ApiException {

        Personnel personnel = findOne(personnelId);

        Optional<Allowance> opAllowances = allowancesRepository.findById(allowancesId);
        if (!opAllowances.isPresent()) {
            throw new ApiException(400, Exceptions.ALLOWANCES_NOT_FOUND);
        }
        Allowance allowance = opAllowances.get();

        List<Allowance> allowances = personnel.getAllowances();
        if (!allowances.contains(allowance)) {
            throw new ApiException(400, Exceptions.ALLOWANCES_NOT_FOUND);
        }

        allowancesRepository.delete(allowance);
        return true;
    }
    /*====================================ALLOWANCES END==============================================================*/

    public ByteArrayInputStream exportPersonnelToExcel(
            Credentials credentials, FindAllPersonnelExcelRequest request
    ) throws ApiException {
        final String COLUMN_ORDINAL_NUMBER = "STT";
        final String COLUMN_ID = "Mã nhân viên";
        final String COLUMN_FULL_NAME = "Họ tên";
        final String COLUMN_IDENTIFICATION = "Chứng minh nhân dân";
        final String COLUMN_WORKING_TIME = "Thời gian làm việc";
        final String COLUMN_PHONE_NUMBER = "Số điện thoại";
        final String COLUMN_WORKING_BANK = "Thông tin ngân hàng";
        final String COLUMN_GENDER = "Giới tính";
        final String COLUMN_EMAIL = "Email";
        final String COLUMN_POSITION = "Vị trí";
        final String COLUMN_DEPARTMENT = "Phòng ban";

        String[] headers = {
                COLUMN_ID, COLUMN_FULL_NAME, COLUMN_IDENTIFICATION, COLUMN_WORKING_TIME, COLUMN_PHONE_NUMBER,
                COLUMN_WORKING_BANK, COLUMN_GENDER, COLUMN_EMAIL, COLUMN_POSITION, COLUMN_DEPARTMENT, COLUMN_ORDINAL_NUMBER
        };
        List<String> columns = request.getColumns();
        if (!columns.contains(COLUMN_ORDINAL_NUMBER)) {
            columns.add(0, COLUMN_ORDINAL_NUMBER);
        }

        boolean validate = columns.stream().anyMatch(each -> !ArrayUtils.contains(headers, each));
        if (validate) {
            String message = "Each item in columns must be any of: " + Arrays.toString(headers);
            throw new ApiException(400, message);
        }
        String SHEET = "Danh sách nhân viên";

        try {
            request.setLimit(Integer.MAX_VALUE);
            FindAllPersonnelRequest findAll = modelMapper.map(request, FindAllPersonnelRequest.class);
            Page<Personnel> page = findMany(credentials, findAll);
            List<Personnel> personnelList = page.getContent();

            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Sheet sheet = workbook.createSheet(SHEET);

            // HEADER
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i));

                Font font = workbook.createFont();
                font.setBold(true);
                CellStyle style = workbook.createCellStyle();
                style.setFont(font);

                cell.setCellStyle(style);
            }

            // ROWS
            int rowIdx = 1;
            for (Personnel personnel : personnelList) {
                Row row = sheet.createRow(rowIdx++);

                for (int j = 0; j < columns.size(); j++) {
                    String column = columns.get(j);
                    String value = "";

                    switch (column) {
                        case COLUMN_ID: {
                            value = String.format("%04d", personnel.getId());
                            break;
                        }
                        case COLUMN_FULL_NAME: {
                            value = personnel.getFullName();
                            break;
                        }
                        case COLUMN_IDENTIFICATION: {
                            if (personnel.getIdentification() != null) {
                                value = personnel.getIdentification().getNumber();
                            }
                            break;
                        }
                        case COLUMN_PHONE_NUMBER: {
                            value = personnel.getPhoneNumber();
                            break;
                        }
                        case COLUMN_EMAIL: {
                            value = personnel.getEmail();
                            break;
                        }
                        case COLUMN_GENDER: {
                            Gender gender = personnel.getGender();
                            if (gender == null) {
                                value = "";
                            }
                            if (gender == Gender.FEMALE) {
                                value = "Nữ";
                            }
                            if (gender == Gender.MALE) {
                                value = "Nam";
                            }
                            break;
                        }
                        case COLUMN_POSITION: {
                            if (personnel.getPosition() != null) {
                                value = personnel.getPosition();
                            }
                            break;
                        }
                        case COLUMN_DEPARTMENT: {
                            if (personnel.getDepartment() != null) {
                                value = personnel.getDepartment().getName();
                            }
                            break;
                        }
                        case COLUMN_ORDINAL_NUMBER: {
                            value = String.format("%04d", rowIdx - 1);
                            break;
                        }
                    }

                    Cell cell = row.createCell(j);
                    cell.setCellValue(value == null ? "" : value);

                    CellStyle style = workbook.createCellStyle();
                    style.setWrapText(true);

                    cell.setCellStyle(style);
                }

                for (int j = 0; j < columns.size(); j++) {
                    sheet.autoSizeColumn(j);
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new ApiException(500, "Fail to export data");
        }
    }

    public List<SalaryView> calculateSalary(
            Credentials credentials, FindSalaryRequest findRequest
    ) throws ApiException {
        // validate input
        List<String> dateStringList = findRequest.getDates();
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
        FindAllPersonnelRequest findAll = modelMapper.map(findRequest, FindAllPersonnelRequest.class);
        findAll.setLimit(Integer.MAX_VALUE);
        Page<Personnel> page = findMany(credentials, findAll);
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

        List<SalaryView> result = new ArrayList<>();
        for (Personnel personnel : personnelList) {
            List<TimeKeeping> timeKeepingList = subs.get(personnel.getId());
            Integer timeOff = 0, timeOn = 0, late = 0, allowance = 0;
            Double advance = 0.0;
            List<Allowance> allowances = personnel.getAllowances();

            for (Allowance each : allowances) {
                allowance += each.getAmount().intValue();
            }

            for (TimeKeeping each : timeKeepingList) {
                Request request = each.getRequest();
                if (each.getStatus().equals("Vắng mặt")
                        && (request == null || !request.getStatus().equals("Chấp thuận"))) {
                    timeOff++;
                }
                if (each.getStatus().equals("Đúng giờ")) {
                    timeOn++;
                }
                if (each.getStatus().equals("Vào trễ")) {
                    late++;
                }
            }

            FindAllRequests findAllRequests = new FindAllRequests();
            findAllRequests.setDecidedDates(dateList);
            findAllRequests.setPersonnelId(personnel.getId());
            findAllRequests.setStatus("Chấp thuận");
            findAllRequests.setLimit(Integer.MAX_VALUE);
            List<Request> advanceRequest = requestsService.findMany(credentials, findAllRequests).getContent();
            for (Request each : advanceRequest) {
                advance += each.getAmount();
            }

            Salary salary = personnel.getSalary();
            Double coefficient = 2.0;
            Double baseSalary = 4000000.0;
            final Integer LATE_FARE = 30000;

            if (salary != null) {
                baseSalary = salary.getBaseSalary();
                coefficient = salary.getCoefficient();
            }

            Double monthSalary = coefficient * baseSalary;
            Double daySalary = monthSalary / 22;

            Double salaryTax = monthSalary - (late * LATE_FARE + timeOff * daySalary);
            Double taxRate = 0.5;
            if (salaryTax <= 5000000) {
                taxRate = 0.05;
            } else if (salaryTax <= 10000000) {
                taxRate = 0.1;
            } else if (salaryTax <= 18000000) {
                taxRate = 0.15;
            } else if (salaryTax <= 32000000) {
                taxRate = 0.2;
            } else if (salaryTax <= 52000000) {
                taxRate = 0.25;
            } else if (salaryTax <= 80000000) {
                taxRate = 0.3;
            } else {
                taxRate = 0.35;
            }
            Double tax = salaryTax * taxRate;
            Double totalSalary = salaryTax + allowance - tax;

            result.add(new SalaryView(
                    personnel, timeOn, timeOff, late, allowance, late * LATE_FARE, advance.intValue(),
                    totalSalary.intValue()));
        }
        return result;
    }
}


