package com.kinart.stock.business.repository;

import com.kinart.stock.business.model.LigneCommandeClient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {


  List<LigneCommandeClient> findAllByCommandeClientId(Integer id);

  List<LigneCommandeClient> findAllByArticleId(Integer id);
}
