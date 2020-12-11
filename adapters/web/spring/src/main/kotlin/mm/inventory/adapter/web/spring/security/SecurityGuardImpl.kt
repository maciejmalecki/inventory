package mm.inventory.adapter.web.spring.security

import mm.inventory.domain.shared.security.Role
import mm.inventory.domain.shared.security.SecurityGuard

class SecurityGuardImpl : SecurityGuard {
    // TODO: implement
    override fun hasRole(role: Role): Boolean = true
}