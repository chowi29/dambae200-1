package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigaretteList.repository.CigaretteListRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListDto;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CigaretteOnListFindService {

    private final CigaretteListRepository cigaretteListRepository;
    private final CigaretteOnListRepository cigaretteOnListRepository;

    //리스트에 있는 담배 보여주기(진열순서)
    public CigaretteOnListDto.GetListCigaretteResponse findAllByCigaretteListIdOrderByDisplay(Long cigaretteListId) {
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByCigaretteListIdOrderByDisplay(cigaretteListId);
        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnLists);
    }

    //리스트에 있는 담배 보여주기(전산순서)
    public CigaretteOnListDto.GetListCigaretteResponse findAllByCigaretteListIdOrderByComputerized(Long cigaretteListId) {
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByCigaretteListIdOrderByComputerized(cigaretteListId);
        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnLists);
    }

    /*
    public CigaretteOnListDto.GetListCigaretteResponse findAllByOfficialName(Long cigaretteListId, String officialName) {
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByOfficialNameLike(cigaretteListId, officialName);
        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnLists);
    }

    public CigaretteOnListDto.GetListCigaretteResponse findAllByNotIncludeOfficialName(Long cigaretteListId, String officialName) {
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByNotIncludeOfficialNameLike(cigaretteListId, officialName);
        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnLists);
    }
    */



}
