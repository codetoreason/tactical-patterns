package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.result.OperationResult;

interface ApplicantValidator {

    OperationResult validate(ApplicantProfile profile);

}

