package mm.inventory.domain.shared.security

class SecurityViolationException(msg: String): RuntimeException(msg)

open class Role(val name: String)

fun interface SecureHandler<T> {
    fun accept(): T
}

interface SecurityGuard {

    fun <T> withRole(role: Role, handler: SecureHandler<T>): T

    fun <T> withAnyRole(vararg roles: Role, handler: SecureHandler<T>): T

    fun <T> withAllRoles(vararg  roles: Role, handler: SecureHandler<T>): T
}