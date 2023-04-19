package dream.command

enum class ResultStatsType(val id: Int, val type: String) {
   SUCCESS_COUNT(0, "SuccessCount"),
   AFFECTED_BLOCKS(1, "AffectedBlocks"),
   AFFECTED_ENTITIES(2, "AffectedEntities"),
   AFFECTED_ITEMS(3, "AffectedItems"),
   QUERY_RESULT(4, "QueryResult");
}
