package mm.inventory.domain.production

interface ProjectSelector {
    fun findByCode(projectCode: String): Project?
}