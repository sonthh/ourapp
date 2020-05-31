package com.son.service;

import com.son.constant.Exceptions;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.son.util.spec.SearchOperation.*;

@Service
@RequiredArgsConstructor
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
    private final SalaryRepository salaryRepository;

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
    public static ByteArrayInputStream exportPersonnel() {
        String[] HEADERs = {"Id", "Title", "Description", "Published"};
        String SHEET = "Danh sách nhân v";

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
}


