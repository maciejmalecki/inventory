package mm.inventory.domain.shared.security

class SecurityViolationException(msg: String) : RuntimeException(msg)

open class Role(val name: String)

fun interface SecureHandler<T> {
    fun accept(): T
}

/**
 * Port for role based security.
 */
interface SecurityGuard {

    /**
     * Has current user a given role?
     */
    fun hasRole(role: Role): Boolean

    /**
     * Ensures that current user has a given role while executing the handler.
     */
    fun <T> withRole(role: Role, handler: SecureHandler<T>): T = withAllRoles(role, handler = handler)

    /**
     * Ensures that current user has any of required roles while executing the handler.
     */
    fun <T> withAnyRole(vararg roles: Role, handler: SecureHandler<T>): T =
        if (roles.any { role -> hasRole(role) }) {
            handler.accept()
        } else {
            throw SecurityViolationException("Access denied, at least one role required: " + rolesToString(*roles) + ".")
        }

    /**
     * Ensures that current user has all required roles while executing the handler.
     */
    fun <T> withAllRoles(vararg roles: Role, handler: SecureHandler<T>): T =
        if (roles.all { role -> hasRole(role) }) {
            handler.accept()
        } else {
            throw SecurityViolationException("Access denied, lack of all required roles: " + rolesToString(*roles) + ".")
        }

    private fun rolesToString(vararg roles: Role) = roles.map { role -> role.name }.joinToString(", ")
}