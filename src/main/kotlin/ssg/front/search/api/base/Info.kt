package ssg.front.search.api.base

class Info {
    // 생성자들

    var predicate: String? = null
    var operator = 1
    var start = 0
    var count = 1
    var categoryDepth: String? = null
    var sortList: List<Sort>? = null

    // 생성자들
    constructor() {}

    constructor(predicate: String) {
        this.predicate = predicate
    }

    constructor(operator: Int) {
        this.operator = operator
    }

    constructor(predicate: String, operator: Int) {
        this.predicate = predicate
        this.operator = operator
    }

    constructor(start: Int, count: Int) {
        this.start = start
        this.count = count
    }

    constructor(sortList: List<Sort>) {
        this.sortList = sortList
    }

    constructor(predicate: String, categoryDepth: String) {
        this.predicate = predicate
        this.categoryDepth = categoryDepth
    }
}
