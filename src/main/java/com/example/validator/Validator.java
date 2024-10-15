package com.example.validator;

import com.example.dto.PackageRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Validator {
    public void validate(PackageRequest packageRequest) throws BadRequestException {
        if(Objects.isNull(packageRequest)){
            throw new BadRequestException("packageRequest cannot be null");
        }

        else if(null == packageRequest.getPackageName() || packageRequest.getPackageName().isEmpty()){
            throw new BadRequestException("packageName cannot be null or empty");
        }
        else if(null == packageRequest.getReceiverId() || packageRequest.getReceiverId().isEmpty()){
            throw new BadRequestException("receiverId cannot be null or empty");
        }
        else if(packageRequest.getWeight() == 0){
            throw new BadRequestException("weight cannot be zero");
        }
    }

    public void validate(String id, String name) throws BadRequestException {
        if(null == id || id.isEmpty()){
            throw new BadRequestException(name+" cannot be null or empty");
        }
    }

}
