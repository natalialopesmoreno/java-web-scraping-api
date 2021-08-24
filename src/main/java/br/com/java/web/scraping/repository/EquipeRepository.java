package br.com.java.web.scraping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.java.web.scraping.entity.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {

}
