package io.demo.paymentsystem.api.lifecycle.check

class DocOperationCheckResult(
        val isSuccessful: Boolean,
        val errors: List<String> = arrayListOf()
) {
}