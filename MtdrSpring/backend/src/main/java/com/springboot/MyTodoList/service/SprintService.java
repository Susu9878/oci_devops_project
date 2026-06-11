package com.springboot.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.repository.SprintRepository;

@Service
public class SprintService {
    @Autowired
    private SprintRepository sprintRepository;

    public Sprint findActiveSprintByUserId(Integer userId) {

        return sprintRepository.findByStatus(Sprint.SprintStatus.ACTIVE)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
