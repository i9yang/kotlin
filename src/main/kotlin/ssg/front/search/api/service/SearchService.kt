package ssg.front.search.api.service

import org.springframework.stereotype.Service
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

@Service
class SearchService : BaseService() {
    fun search(parameter : Parameter): Result {
        return super.iterateBuildersAndExecute(parameter)
    }
}