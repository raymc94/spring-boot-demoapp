package demoapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.RecetaFavorita;
import demoapp.model.RecetaFavoritaRepository;

@Service
public class RecetaFavoritaService {

    Logger logger = LoggerFactory.getLogger(RecetaFavoritaService.class);


    private RecetaFavoritaRepository recetaFavoritaRepository;
    
    @Autowired
    public RecetaFavoritaService(RecetaFavoritaRepository recetaFavoritaRepository) {

        this.recetaFavoritaRepository = recetaFavoritaRepository;
    }

    @Transactional
    public RecetaFavorita nuevaRecetaFavorita(RecetaFavorita recetaFavorita) {

    	recetaFavoritaRepository.save(recetaFavorita);
        return recetaFavorita;
    }

    @Transactional
    public void borraRecetaFavorita(RecetaFavorita recetaFavorita) {
    	
    	recetaFavoritaRepository.delete(recetaFavorita);
    }
}
