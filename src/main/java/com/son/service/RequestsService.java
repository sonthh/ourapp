package com.son.service;

import com.son.constant.Exceptions;
import com.son.dto.CountRequest;
import com.son.entity.Personnel;
import com.son.entity.Request;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.RequestsRepository;
import com.son.request.AddRequest;
import com.son.request.FindAllRequests;
import com.son.request.UpdateRequest;
import com.son.security.Credentials;
import com.son.util.page.PageUtil;
import com.son.util.spec.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.son.util.spec.SearchOperation.*;

@Service
@RequiredArgsConstructor
public class RequestsService {
    private final RequestsRepository requestsRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PersonnelService personnelService;

    public Request findOne(Integer requestsId) throws ApiException {
        Optional<Request> requests = requestsRepository.findById(requestsId);

        if (!requests.isPresent()) {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_FOUND);
        }

        return requests.get();
    }

    public Request createOneRequest(
            AddRequest addRequest, Credentials credentials
    ) throws ApiException {
        User receiver = userService.findOne(addRequest.getReceiverId());
        Personnel personnel = null;

        if (addRequest.getPersonnelId() != null) {
            personnel = personnelService.findOne(addRequest.getPersonnelId());
        }

        if (credentials.getId().equals(receiver.getId())) {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_INVALID);
        }

        Request request = modelMapper.map(addRequest, Request.class);

        request.setReceiver(receiver);
        request.setPersonnel(personnel);

        return requestsRepository.save(request);
    }

    public Boolean deleteOneRequest(Integer requestsId, Credentials credentials) throws ApiException {
        Request request = findOne(requestsId);

        requestsRepository.delete(request);
        return true;
    }

    public Request updateOneRequest(
            Integer requestId, UpdateRequest updateRequest, Credentials credentials
    ) throws ApiException {
        Request request = findOne(requestId);
        User receiver = null;

        if (updateRequest.getReceiverId() != null) {
            receiver = userService.findOne(updateRequest.getReceiverId());
        }

        modelMapper.map(updateRequest, request);
        request.setReceiver(receiver);

        return requestsRepository.save(request);
    }

    public Page<Request> findMany(Credentials credentials, FindAllRequests findAllRequests) throws ApiException {
        Integer currentPage = findAllRequests.getCurrentPage();
        Integer limit = findAllRequests.getLimit();
        String receiver = findAllRequests.getReceiver();

        String sortDirection = findAllRequests.getSortDirection();
        String sortBy = findAllRequests.getSortBy();
        String createdBy = findAllRequests.getCreatedBy();
        String lastModifiedBy = findAllRequests.getLastModifiedBy();
        String type = findAllRequests.getType();
        String reason = findAllRequests.getReason();
        String status = findAllRequests.getStatus();

        List<Integer> ids = findAllRequests.getIds();

        SpecificationBuilder<Request> builder = new SpecificationBuilder<>();
        builder.query("id", IN, ids)
                .query("reason", CONTAINS, reason)
                .query("status", CONTAINS, status)
                .query("type", CONTAINS, type)
                .query("createdBy.username", CONTAINS, createdBy)
                .query("lastModifiedBy.username", CONTAINS, lastModifiedBy)
                .query("receiver.name", EQUALITY, receiver);

        Specification<Request> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return requestsRepository.findAll(spec, pageable);
    }

    public CountRequest countByStatus(Credentials credentials) throws ApiException {
        return new CountRequest(
                requestsRepository.countByStatus("Chờ phê duyệt"),
                requestsRepository.countByStatus("Châp thuận"),
                requestsRepository.countByStatus("Từ chối")
        );
    }
}
