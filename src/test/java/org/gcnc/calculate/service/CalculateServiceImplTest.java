package org.gcnc.calculate.service;

//@ExtendWith(MockitoExtension.class)
//public class CalculateServiceImplTest {
//    @InjectMocks
//    private CalculateServiceImpl calculateService;
//
//
//    @Test
////    public void fetchRanking() {
//        // Given
//        Map<Integer, List<TeamResult>> results = Map.of(
//                1, List.of(new TeamResult("A", 3),
//                        new TeamResult("B", 2),
//                        new TeamResult("C", 3),
//                        new TeamResult("D", 1)),
//                2, List.of(new TeamResult("A", null),
//                        new TeamResult("B", null),
//                        new TeamResult("C", null),
//                        new TeamResult("D", null))
//            );
//        Map<String, Integer> rankings = Map.of("A", 1, "B", 3, "C", 1, "D", 0);
//
//        when(fetcher.fetchResponse(anyString())).thenReturn(Mono.just("response"));
//        when(parser.getResults(any())).thenReturn(Mono.just(results));
//        when(parser.getPoints(any())).thenReturn(Mono.just(rankings));
//
//        // When/Then
//        StepVerifier.create(calculateService.calculateResponse(request))
//                .assertNext(teamRank -> {
//                    assertEquals("B", teamRank.getTeam());
//                    assertEquals(Double.parseDouble("1.0"), teamRank.getEvPoints());
//                })
//                .assertNext(teamRank -> {
//                    assertEquals("A", teamRank.getTeam());
//                    assertEquals(Double.parseDouble("2.3333333333333335"), teamRank.getEvPoints());
//                })
//                .assertNext(teamRank -> {
//                    assertEquals("C", teamRank.getTeam());
//                    assertEquals(Double.parseDouble("2.3333333333333335"), teamRank.getEvPoints());
//                })
//                .assertNext(teamRank -> {
//                    assertEquals("D", teamRank.getTeam());
//                    assertEquals(Double.parseDouble("0"), teamRank.getEvPoints());
//                })
//                .verifyComplete();
//    }

//}