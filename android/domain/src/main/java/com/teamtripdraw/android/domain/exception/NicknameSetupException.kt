package com.teamtripdraw.android.domain.exception

sealed class NicknameSetupException : Exception()
class InvalidNicknameException(override val message: String?) : NicknameSetupException()
class DuplicateNicknameException(override val message: String) : NicknameSetupException()
