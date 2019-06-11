package ssg.front.search.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ssg.front.common.logging.MonitorInformation
import ssg.front.search.api.core.dto.DataApiResDto
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.service.SearchService

@RestController
class SearchController {
    @Autowired
    lateinit var searchService : SearchService

    @PostMapping(path = ["/search/{target}"], consumes= [MediaType.APPLICATION_JSON_VALUE])
    private fun search(@PathVariable("target") target: String,
                       @RequestBody parameter : Parameter
    ) : DataApiResDto<Any> {
        var result = searchService.search(parameter)

        return if(result.libErr) {
            MonitorInformation.sendTelegramLogFile(result.libErrMsg, "$target search engine api Error!")
            DataApiResDto.newFailure(result.libErrMsg)
        } else {
            DataApiResDto.newSuccess(result)
        }
    }
}