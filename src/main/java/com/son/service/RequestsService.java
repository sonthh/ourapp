package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.Requests;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.RequestsRepository;
import com.son.request.AddRequests;
import com.son.request.FindAllRequests;
import com.son.request.UpdateConfirmRequests;
import com.son.request.UpdateRequests;
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


    /*====================================REQUESTS START==============================================================*/
    public Requests findOne(Integer requestsId) throws ApiException {
        Optional<Requests> requests = requestsRepository.findById(requestsId);

        if (!requests.isPresent()) {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_FOUND);
        }

        return requests.get();
    }

    public Requests createRequests(AddRequests addRequests, Credentials credentials) throws ApiException {
        User receiver = userService.findOne(addRequests.getReceiverId());

        if (credentials.getId().equals(receiver.getId())) {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_INVALID);
        }

        Requests requests = modelMapper.map(addRequests, Requests.class);
        requests.setReceiver(receiver);

        return requestsRepository.save(requests);
    }

    public Boolean deleteRequests(Integer requestsId, Credentials credentials) throws ApiException {
        Requests requests = findOne(requestsId);

        if (credentials.getUserEntity().getUsername().equals("admin")
            || credentials.getId().equals(requests.getReceiver().getId())) {
            requestsRepository.delete(requests);
            return true;
        } else {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_INVALID);
        }
    }

    public Requests updateRequests(Integer requestsId, UpdateRequests updateRequests, Credentials credentials)
        throws ApiException {
        Requests requests = findOne(requestsId);
        User receiver = userService.findOne(updateRequests.getReceiverId());

        if (credentials.getId().equals(receiver.getId())) {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_INVALID);
        }

        if (!requests.getCreatedBy().getId().equals(credentials.getId())) {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_INVALID);
        }
        modelMapper.map(updateRequests, requests);
        requests.setReceiver(receiver);
        return requestsRepository.save(requests);
    }

    public Requests confirmRequests(UpdateConfirmRequests updateConfirmRequests, Credentials credentials)
        throws ApiException {
        Requests requests = findOne(updateConfirmRequests.getRequestsId());

        if (credentials.getUserEntity().getUsername().equals("admin")
            || credentials.getId().equals(requests.getReceiver().getId())) {
            requests.setStatus(updateConfirmRequests.getStatus());
            requests.setConfirmBy(credentials.getUserEntity());
            return requestsRepository.save(requests);
        } else {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_INVALID);
        }
    }

    public Page<Requests> findMany(Credentials credentials, FindAllRequests findAllRequests) throws ApiException {
        Integer currentPage = findAllRequests.getCurrentPage();
        Integer limit = findAllRequests.getLimit();
        Integer receiverId = findAllRequests.getReceiverId();
        Integer confirmBy = findAllRequests.getConfirmBy();

        String sortDirection = findAllRequests.getSortDirection();
        String sortBy = findAllRequests.getSortBy();
        String createdBy = findAllRequests.getCreatedBy();
        String lastModifiedBy = findAllRequests.getLastModifiedBy();
        String type = findAllRequests.getType();
        String reason = findAllRequests.getReason();
        String status = findAllRequests.getStatus();

        List<Integer> ids = findAllRequests.getIds();

        SpecificationBuilder<Requests> builder = new SpecificationBuilder<>();
        builder.query("id", IN, ids)
            .query("reason", CONTAINS, reason)
            .query("status", CONTAINS, status)
            .query("type", CONTAINS, type)
            .query("createdBy.username", CONTAINS, createdBy)
            .query("lastModifiedBy.username", CONTAINS, lastModifiedBy)
            .query("receiver.id", EQUALITY, receiverId)
            .query("confirmBy.id", EQUALITY, confirmBy);

        Specification<Requests> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return requestsRepository.findAll(spec, pageable);
    }
    /*====================================REQUESTS END================================================================*/
}
