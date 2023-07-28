package com.teamtripdraw.android.domain.exception

sealed class NicknameSetupException : Exception()
class InvalidNickNameException(override val message: String?) : NicknameSetupException()
class DuplicateNickNameException(override val message: String) : NicknameSetupException()
