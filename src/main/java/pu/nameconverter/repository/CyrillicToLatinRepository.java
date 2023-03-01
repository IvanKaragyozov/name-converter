package pu.nameconverter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pu.nameconverter.entity.CyrillicToLatin;

import java.util.Optional;

@Repository
public interface CyrillicToLatinRepository extends JpaRepository<CyrillicToLatin, Long> {

    Optional<CyrillicToLatin> findByCyrillicName(String cyrillicName);
    //CyrillicToLatin findByCyrillicName(String cyrillicName);
}
