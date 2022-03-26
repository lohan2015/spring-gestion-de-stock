package com.cmbassi.gestiondestock.repository;

import com.cmbassi.gestiondestock.model.LigneCommandeClient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {


  List<LigneCommandeClient> findAllByCommandeClientId(Integer id);

  List<LigneCommandeClient> findAllByArticleId(Integer id);
}
