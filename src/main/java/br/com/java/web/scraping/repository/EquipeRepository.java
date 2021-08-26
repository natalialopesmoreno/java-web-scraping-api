package br.com.java.web.scraping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.java.web.scraping.entity.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
	
	public Optional<Equipe> findByNomeEquipe(String nome);
	
	public boolean existsByNomeEquipe(String nome);

}
