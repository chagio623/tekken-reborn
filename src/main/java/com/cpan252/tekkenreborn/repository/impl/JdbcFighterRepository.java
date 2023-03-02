package com.cpan252.tekkenreborn.repository.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cpan252.tekkenreborn.model.Fighter;
import com.cpan252.tekkenreborn.model.Fighter.Anime;
import com.cpan252.tekkenreborn.repository.FighterRepository;

@Repository
public class JdbcFighterRepository implements FighterRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcFighterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Fighter> findAll() {
        String query = "SELECT * FROM fighter";
        RowMapper<Fighter> rowMapper = new FighterRowMapper();
        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public Optional<Fighter> findById(Long id) {
        String query = "SELECT * FROM fighter WHERE id = ?";
        RowMapper<Fighter> rowMapper = new FighterRowMapper();
        Fighter fighter = jdbcTemplate.queryForObject(query, rowMapper, id);
        return Optional.ofNullable(fighter);
    }

    @Override
    public <S extends Fighter> S save(S fighter) {
        String query = "INSERT INTO fighter (name, damage_per_hit, health, resistance, anime_from, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(query, new String[] { "id" });
            ps.setString(1, fighter.getName());
            ps.setInt(2, fighter.getDamagePerHit());
            ps.setInt(3, fighter.getHealth());
            ps.setBigDecimal(4, fighter.getResistance());
            ps.setString(5, fighter.getAnimeFrom().name());
            ps.setString(6, fighter.getCreatedAt().toString());
            return ps;
        }, keyHolder);

        Long id = (Long) keyHolder.getKey();
        fighter.setId(id);

        return fighter;
    }

    @Override
    public <S extends Fighter> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public boolean existsById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public Iterable<Fighter> findAllById(Iterable<Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void delete(Fighter entity) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Fighter> entities) {

        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM fighter";
        jdbcTemplate.update(query);
    }

    @Override
    public List<Fighter> findByAnimeFrom(Anime anime) {
        String query = "SELECT * FROM fighter WHERE anime_from = ?";
        RowMapper<Fighter> rowMapper = new FighterRowMapper();
        return jdbcTemplate.query(query, rowMapper, anime.name());
    }

    @Override
    public List<Fighter> findByNameStartsWithAndCreatedAtBetween(String name, LocalDate startDate, LocalDate endDate) {
        String query = "SELECT * FROM fighter WHERE name LIKE ? AND created_at BETWEEN ? AND ?";
        RowMapper<Fighter> rowMapper = new FighterRowMapper();
        return jdbcTemplate.query(query, rowMapper, name + "%", startDate, endDate);
    }
}
