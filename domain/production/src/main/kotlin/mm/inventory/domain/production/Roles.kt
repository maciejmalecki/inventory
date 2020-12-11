package mm.inventory.domain.production

import mm.inventory.domain.shared.security.Role

val PRODUCTION_ROLE = object : Role("production") {}
val PRODUCTION_WRITER_ROLE = object : Role("production/writer") {}