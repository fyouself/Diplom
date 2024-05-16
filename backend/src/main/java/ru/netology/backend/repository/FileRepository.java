package ru.netology.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.netology.backend.model.FilesModel;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FilesModel, Long> {
    @Transactional
    @Query(value = "SELECT * from Files where files_id = ?1 LIMIT ?2", nativeQuery = true)
    List<FilesModel> getList(long userId, int limit);

    @Transactional
    @Query(value = "SELECT * from files where name = ?1 and files_id = ?2", nativeQuery = true)
    FilesModel downloadFile(String filename, long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update files set name = ?2 where name = ?1 and files_id = ?3", nativeQuery = true)
    void editName(String filename, String ediFilename, long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM files WHERE name = ?1 and files_id = ?2", nativeQuery = true)
    void deleteFile(String filename, long id);
}