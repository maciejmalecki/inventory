package mm.inventory.adapter.web.spring.security

import mm.inventory.domain.shared.security.Role
import mm.inventory.domain.shared.security.SecureHandler
import mm.inventory.domain.shared.security.SecurityGuard

// TODO: implement
class SecurityGuardImpl: SecurityGuard {
    override fun <T> withRole(role: Role, handler: SecureHandler<T>): T = handler.accept()

    override fun <T> withAnyRole(vararg roles: Role, handler: SecureHandler<T>): T = handler.accept()

    override fun <T> withAllRoles(vararg roles: Role, handler: SecureHandler<T>): T = handler.accept()
}