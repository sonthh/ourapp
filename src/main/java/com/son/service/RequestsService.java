package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.Requests;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.RequestsRepository;
import com.son.request.AddRequests;
import com.son.request.UpdateRequests;
import com.son.security.Credentials;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        if (!credentials.getId().equals(requests.getReceiver().getId())) {
            throw new ApiException(400, Exceptions.REQUESTS_NOT_INVALID);
        }

        requestsRepository.delete(requests);
        return true;
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
    /*====================================REQUESTS END================================================================*/
}
