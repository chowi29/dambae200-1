package com.dambae200.dambae200.domain.store.controller;

import com.dambae200.dambae200.domain.store.dto.StoreDto;
import com.dambae200.dambae200.domain.store.service.StoreFindService;
import com.dambae200.dambae200.domain.store.service.StoreUpdateService;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import com.dambae200.dambae200.global.common.DeleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URLDecoder;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreRestController {

    final StoreFindService storeFindService;
    final StoreUpdateService storeUpdateService;


    @GetMapping("")
    public ResponseEntity<StoreDto.GetListResponse> findAllByName(@RequestParam @NotNull String name){
        String decodedName = URLDecoder.decode(name, Charset.forName("UTF-8"));
        StoreDto.GetListResponse response = storeFindService.findByNameLike(decodedName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<StoreDto.GetResponse> addStore(@RequestBody @Valid StoreDto.AddRequest request){
        StoreDto.GetResponse response = storeUpdateService.addStore(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDto.GetResponse> updateStore(@PathVariable @NotNull Long id, @RequestBody @Valid StoreDto.UpdateRequest request){
        StoreDto.GetResponse response = storeUpdateService.updateStore(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteStore(@PathVariable String id){
        DeleteResponse response = storeUpdateService.deleteStore(Long.valueOf(id));
        return ResponseEntity.ok(response);
    }





}
