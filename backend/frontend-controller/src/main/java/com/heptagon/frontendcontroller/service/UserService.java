package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.UserNotFoundException;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.UserRepository;
import com.heptagon.frontendcontroller.util.CustomBeanUtils;
import com.heptagon.thirema.commons.domain.User;
import com.heptagon.thirema.commons.event.EntityEvent;
import com.heptagon.thirema.commons.event.EntityState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserService {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    public List<User> getUserList() {
        return (List<User>) userRepository.findAll();
    }

    public void addUser(User user) {
        checkUserAlreadyExists(user);
        User saved = userRepository.save(user);
        publishUserEvent(saved, EntityState.CREATED);
    }

    public void updateUser(User user) {
        Optional<User> opt = userRepository.findById(user.getId());
        if (opt.isPresent()) {
            User oldUser = opt.get();
            checkUserAlreadyExists(user,
                    oldUser.getUsername().equals(user.getUsername()),
                    oldUser.getEmail().equals(user.getEmail()),
                    oldUser.getTelegramUsername().equals(user.getTelegramUsername()));
            try {
                CustomBeanUtils.copyPropertiesNotNull(user, oldUser);
            } catch (InvocationTargetException | IllegalAccessException ignored) {
                // non accadrà mai perché sono oggetti dello stesso tipo
            }
            User saved = userRepository.save(oldUser);
            publishUserEvent(saved, EntityState.UPDATED);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void deleteUser(long id) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            userRepository.deleteById(id);
            publishUserEvent(opt.get(), EntityState.DELETED);
        } else {
            throw new UserNotFoundException();
        }
    }

    private void checkUserAlreadyExists(User user, boolean sameUsername, boolean sameEmail,
                                        boolean sameTelegramUsername) {
        List<String> fields = new ArrayList<>();
        if (!sameUsername && userRepository.existsByUsername(user.getUsername())) {
            fields.add("username");
        }
        if (!sameEmail && userRepository.existsByEmail(user.getEmail())) {
            fields.add("email");
        }
        if (!sameTelegramUsername && userRepository.existsByTelegramUsername(user.getTelegramUsername())) {
            fields.add("telegramUsername");
        }
        if (!fields.isEmpty()) {
            throw new AlreadyExistsException(fields);
        }
    }

    private void checkUserAlreadyExists(User user) {
        checkUserAlreadyExists(user, false, false, false);
    }

    private void publishUserEvent(User user, EntityState state) {
        EntityEvent<User> event = new EntityEvent<>(user, state);
        eventPublisher.publishEvent(EntityEvent.USER_TOPIC, event);
    }
}
