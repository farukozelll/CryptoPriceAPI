package com.backend.sade.service;

import com.backend.sade.entity.User;
import com.backend.sade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Kullanıcıyı kaydetme
    public User saveUser(User user) {
        // Kullanıcı veritabanına kaydedilmeden önce şifre hashleme işlemi yapılabilir.
        return userRepository.save(user);
    }

    // Kullanıcı adıyla kullanıcı bulma
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    // E-posta ile kullanıcı bulma
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    // Kullanıcı bilgilerini güncelleme
    public User updateUser(User updatedUser) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            user.setBirthDate(updatedUser.getBirthDate());
            return userRepository.save(user);
        }
        return null; // Kullanıcı bulunamazsa null döndürülür
    }

    // Kullanıcıyı ID ile silme
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
