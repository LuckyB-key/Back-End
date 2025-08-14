package com.luckyb.domain.shelter.dto;

import com.luckyb.domain.shelter.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    
    private String jibun;
    private String road;
    
    public static AddressDto from(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDto(address.getJibun(), address.getRoad());
    }
    
    public Address toEntity() {
        return Address.of(jibun, road);
    }
} 