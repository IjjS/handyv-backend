package com.programmers.handyV.charger.service;

import org.springframework.stereotype.Service;

import com.programmers.handyV.charger.domain.Charger;
import com.programmers.handyV.charger.dto.request.CreateChargerRequest;
import com.programmers.handyV.charger.dto.response.CreateChargerResponse;
import com.programmers.handyV.charger.repository.ChargerRepository;
import com.programmers.handyV.station.domain.Station;
import com.programmers.handyV.station.repository.StationRepository;

@Service
public class ChargerService {
    private final ChargerRepository chargerRepository;
    private final StationRepository stationRepository;

    public ChargerService(ChargerRepository chargerRepository, StationRepository stationRepository) {
        this.chargerRepository = chargerRepository;
        this.stationRepository = stationRepository;
    }

    public CreateChargerResponse create(CreateChargerRequest request) {
        Station station = stationRepository.findById(request.stationId());
        Charger charger = Charger.createCharger(request.chargerType(), station.getStationId());
        Charger savedCharger = chargerRepository.save(charger);
        return new CreateChargerResponse(savedCharger.getHashName(), station.getName());
    }
}
