package com.demo.api.validator;

import com.demo.api.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpfValidator implements
                              ConstraintValidator<ValidCPF, String> {

    private final UserRepository userRepository;

    public CpfValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(ValidCPF constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cpf,
                           ConstraintValidatorContext cxt) {
        return cpf != null && validateCpf(cpf);
    }

    public boolean validateCpf(String cpf) {
        if (StringUtils.isBlank(cpf)) {
            return false;
        }

        int[] digits = StringUtils.getDigits(cpf)
                                  .chars()
                                  .map(Character::getNumericValue)
                                  .toArray();

        if (digits.length != 11) {
            return false;
        }

        if (hasAllRepeatedDigits(digits)) {
            return false;
        }

        int[] verifier = findCpfVerifierDigits(digits);
        return verifier[0] == digits[9] && verifier[1] == digits[10];
    }

    private static boolean hasAllRepeatedDigits(int[] cpf) {
        int value = cpf[0];
        for (int i = 1; i < cpf.length; i++) {
            if (cpf[i] != value) {
                return false;
            }
        }
        return true;
    }

    private static int[] findCpfVerifierDigits(int[] cpf) {
        int j = 0;
        int k = 0;

        for (int i = 0; i < 9; i++) {
            j += cpf[i] * (10 - i);
            k += cpf[i] * (11 - i);
        }

        j = (j % 11) < 2 ? 0 : 11 - (j % 11);
        k += j * 2;
        k = (k % 11) < 2 ? 0 : 11 - (k % 11);
        return new int[]{j, k};
    }
}