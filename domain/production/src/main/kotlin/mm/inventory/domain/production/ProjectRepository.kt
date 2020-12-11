package mm.inventory.domain.production

interface ProjectRepository {
    fun findByCode(projectCode: String): Project?
}