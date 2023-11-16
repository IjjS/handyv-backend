package com.programmers.handyV.charger.service;

import com.programmers.handyV.charger.domain.Charger;
import com.programmers.handyV.charger.dto.request.BookingRequest;
import com.programmers.handyV.charger.dto.request.CreateChargerRequest;
import com.programmers.handyV.charger.dto.response.ChargerResponse;
import com.programmers.handyV.charger.dto.response.ConductBookingResponse;
import com.programmers.handyV.charger.dto.response.CreateChargerResponse;
import com.programmers.handyV.charger.repository.ChargerRepository;
import com.programmers.handyV.common.exception.BadRequestException;
import com.programmers.handyV.station.domain.Station;
import com.programmers.handyV.station.repository.StationRepository;
import com.programmers.handyV.user.domain.CarNumber;
import com.programmers.handyV.user.domain.User;
import com.programmers.handyV.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChargerService {
    private final ChargerRepository chargerRepository;
    private final StationRepository stationRepository;
    private final UserRepository userRepository;

    public ChargerService(ChargerRepository chargerRepository,
                          StationRepository stationRepository,
                          UserRepository userRepository) {
        this.chargerRepository = chargerRepository;
        this.stationRepository = stationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateChargerResponse create(CreateChargerRequest request) {
        Station station = stationRepository.findById(request.stationId());
        Charger charger = Charger.createCharger(request.chargerType(), station.getStationId());
        Charger savedCharger = chargerRepository.save(charger);
        return new CreateChargerResponse(savedCharger.getHashName(), station.getName());
    }

    @Transactional
    public List<ChargerResponse> enterFindAll(Optional<UUID> stationId) {
        chargerRepository.refreshStatus();
        return stationId.isPresent() ? findByStationId(stationId.get()) : findAll();
    }

    @Transactional(readOnly = true)
    public List<ChargerResponse> findAll() {
        List<Charger> chargers = chargerRepository.findAll();
        return ChargerResponse.listOf(chargers);
    }

    @Transactional(readOnly = true)
    public List<ChargerResponse> findByStationId(UUID stationId) {
        List<Charger> chargers = chargerRepository.findByStationId(stationId);
        return ChargerResponse.listOf(chargers);
    }

    @Transactional
    public ConductBookingResponse conductBooking(UUID chargerId, BookingRequest request) {
        chargerRepository.refreshStatus();
        Charger charger = chargerRepository.findById(chargerId)
                .orElseThrow(() -> new BadRequestException("해당 ID의 충전기가 존재하지 않습니다."));
        validateChargerStatus(charger);

        CarNumber carNumber = new CarNumber(request.frontNumber(), request.backNumber());
        User user = userRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new BadRequestException(carNumber.getFullNumber() + "의 번호로 등록된 차량이 없습니다."));

        charger.conductBooking(user.getUserId());

        Charger savedCharger = chargerRepository.save(charger);
        return ConductBookingResponse.of(savedCharger, carNumber);
    }

    @Transactional
    public ChargerResponse cancelBooking(UUID chargerId, BookingRequest request) {
        chargerRepository.refreshStatus();
        Charger charger = chargerRepository.findById(chargerId)
                .orElseThrow(() -> new BadRequestException("해당 ID의 충전기가 존재하지 않습니다."));

        CarNumber carNumber = new CarNumber(request.frontNumber(), request.backNumber());
        User user = userRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new BadRequestException(carNumber.getFullNumber() + "의 번호로 등록된 차량이 없습니다."));
        validateBooker(charger, user);

        charger.cancelBooking();
        Charger savedCharger = chargerRepository.save(charger);
        return ChargerResponse.from(savedCharger);
    }

    private void validateChargerStatus(Charger charger) {
        if (charger.isBooked()) {
            throw new BadRequestException("이미 예약된 충전기입니다.");
        }
    }

    private void validateBooker(Charger charger, User user) {
        if (!charger.getUserId().equals(user.getUserId())) {
            throw new BadRequestException(user.getCarFullNumber() + "의 차주 분은 "
                    + charger.getHashName() + " 충전기를 예약하지 않았습니다.");
        }
    }
}
