package com.chris.LaboratoryManagement.utils;

import com.chris.LaboratoryManagement.entity.Course;
import com.chris.LaboratoryManagement.entity.User;
import com.chris.LaboratoryManagement.repository.ClassRepository;
import com.chris.LaboratoryManagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class Helper {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    UserRepository userRepository;

    public String generateTempPwd(int length) {
        String numbers = "012345678";
        char otp[] = new char[length];
        Random getOtpNum = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }
        return optCode;
    }

    public String generateNextUserId() {
        Optional<User> lastUser = userRepository.findTopByOrderByIdDesc();
        // Default to "C000001" if no users exist
        if (lastUser.isEmpty() || !lastUser.get().getId().matches("C\\d{6}")) {
            return "C000001";
        }

        // Extract numeric part from last ID, increment, and format
        String lastId = lastUser.get().getId();
        int nextIdNum = Integer.parseInt(lastId.substring(1)) + 1;
        return String.format("C%06d", nextIdNum);
    }

    public String generateNextClassId(String courseId) {
        String lastClassId = classRepository.findLastClassIdByCourseId(courseId); // Assuming this repository method exists

        int nextNumber = 1;

        if (lastClassId != null && lastClassId.startsWith(courseId)) {
            // Extract the numeric suffix from the lastClassId
            String suffix = lastClassId.substring(courseId.length());
            nextNumber = Integer.parseInt(suffix) + 1; // Increment the number
        }

        // Format the new ID with a two-digit suffix (e.g., "01", "02")
        return courseId + String.format("%02d", nextNumber);
    }

}
