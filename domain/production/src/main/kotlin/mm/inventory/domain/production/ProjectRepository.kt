package mm.inventory.domain.production

interface ProjectRepository {
    fun findByCode(projectCode: String): Project?
    fun store(project: Project)
}