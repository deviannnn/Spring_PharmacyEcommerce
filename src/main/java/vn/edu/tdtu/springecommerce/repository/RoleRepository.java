package vn.edu.tdtu.springecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.tdtu.springecommerce.model.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>{
    Role findByName(String name);

}
