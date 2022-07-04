package org.fonke.stockservice.service.imp;


import lombok.extern.slf4j.Slf4j;
import org.fonke.stockservice.dto.ClientDto;
import org.fonke.stockservice.entity.Client;
import org.fonke.stockservice.exeception.EntityNotFoundException;
import org.fonke.stockservice.exeception.ErrorCodes;
import org.fonke.stockservice.exeception.InvalidEntityException;
import org.fonke.stockservice.repository.ClientRepository;
import org.fonke.stockservice.service.ClientService;
import org.fonke.stockservice.validator.ClientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDto save(ClientDto clientDto) {
        List<String> errors = ClientValidator.validate(clientDto);
        if(!errors.isEmpty()){
            log.error("Client is not valid{}",clientDto);
            throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }
        return clientDto.fromEntity(clientRepository.save(
                clientDto.toEntity(clientDto)));
    }


    @Override
    public ClientDto findById(Integer id) {
        if(id==null){
            log.error("Article ID is null");
            return  null;
        }
        Optional<Client> client = clientRepository.findById(id);
        return Optional.of(ClientDto.fromEntity(client.get())).orElseThrow(()->
                new EntityNotFoundException(
                        "Aucun client avec l'ID =" + id + "n'a ete trouve dans la BDD",
                        ErrorCodes.CLIENT_NOT_FOUND)
        );
    }
/*
    @Override
    public ClientDto findByCodeClient(String codeClient) {
        if(!StringUtils.hasLength(codeClient)) {
            log.error("Client CODE is null");
            return null;
        }

        Optional<Client> client = clientRepository.findClientByCodeClient(codeClient);

        return Optional.of(ClientDto.fromEntity(client.get())).orElseThrow(()->
                new EntityNotFoundException(
                        "Aucun client avec le CODE = " + codeClient + "n'a ete trouve dans la BDD",
                        ErrorCodes.CLIENT_NOT_FOUND)
        );
    }
*/
    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto ::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {

        if(id==null){
            log.error("Client ID is null");
            return ;
        }
        clientRepository.deleteById(id);
    }
}