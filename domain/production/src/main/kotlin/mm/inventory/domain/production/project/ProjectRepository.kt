package mm.inventory.domain.production.project

interface ProjectRepository {
    fun findByCode(projectCode: String): Project?
    fun store(project: Project)
}