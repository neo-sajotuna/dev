package com.sparta.newneoboardbuddy.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final ZSetOperations<String, Object> zSetOperations;

    private final ValueOperations<String, Integer> valueOperations;


    // 카드 조회 시 조회수 +1
    public void incrementCardView(String targetTable, Long cardId, Long userId) {

        String userKey = "userview:" + userId + ":" + cardId; // 사용자별 카드 조회 키
        Integer viewCount = valueOperations.get(userKey);


        // 조회수 제한을 두기 위해 사용자가 오늘 이미 조회한 횟수를 확인
        if (viewCount == null || viewCount == 0) {
            valueOperations.set(userKey, 1);
            zSetOperations.incrementScore(targetTable, cardId.toString(), 1);

            setRank(targetTable);
        }

        System.out.println(1);

    }

    private void setRank(String targetTable){
        // 상위 3개 카드 조회
        Set<ZSetOperations.TypedTuple<Object>> topRankedCards = zSetOperations.reverseRangeWithScores(targetTable, 0, 2); // 0부터 2까지 => 상위 3개

        // 상위 카드 ID와 조회수를 cardRank 키에 저장
        for (ZSetOperations.TypedTuple<Object> card : topRankedCards) {
            // 카드 ID와 조회수를 cardRank ZSET에 저장
            zSetOperations.add(targetTable + "Rank", card.getValue(), card.getScore());
        }

//        System.out.println(getCardScore(targetTable + "Rank", cardId.toString()));
    }

    // 특정 키로 ZSET의 데이터 찾기 (특정 멤버의 점수 조회)
    public Double getCardScore(String targetTable, String cardId) {
        return zSetOperations.score(targetTable, cardId);
    }

//    // 인기 순위 상위 n개의 카드 조회
//    public Set<ZSetOperations.TypedTuple<Object>> getTopRankedCards(String targetTable, int topN) {
//        return zSetOperations.reverseRangeWithScores(targetTable, 0, topN - 1);
//    }

    // 특정 ZSET 전체 삭제
    public void clearZSet(List<String> deleteList) {
        for(String delete : deleteList){
            zSetOperations.getOperations().delete(delete); // 해당 키 삭제
        }
    }

    public void clearUser(){
        Set<String> keys = valueOperations.getOperations().keys("userView:*");
        if (keys != null) {
            for (String key : keys) {
                valueOperations.set(key, 0); // 초기화
            }
        }
    }

}