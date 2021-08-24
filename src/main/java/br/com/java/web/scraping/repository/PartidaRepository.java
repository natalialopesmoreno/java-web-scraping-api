package br.com.java.web.scraping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.java.web.scraping.entity.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

}
