package com.parrit.services;

import com.parrit.entities.PairingBoard;
import com.parrit.entities.PairingHistory;
import com.parrit.entities.Person;
import com.parrit.entities.Project;
import com.parrit.repositories.PairingHistoryRepository;
import com.parrit.repositories.ProjectRepository;
import com.parrit.support.MockitoTestBase;
import com.parrit.utilities.CurrentTimeProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PairingServiceTest extends MockitoTestBase {

    @Mock
    PairingHistoryRepository mockPairingHistoryRepository;

    @Mock
    ProjectRepository mockProjectRepository;

    @Mock
    RecommendationService mockRecommendationService;

    @Mock
    CurrentTimeProvider mockCurrentTimeProvider;

    PairingService pairingService;

    Timestamp currentTime = new Timestamp(1456364985548L);

    @Before
    public void setup() {
        pairingService = new PairingService(mockPairingHistoryRepository, mockProjectRepository, mockRecommendationService,
                mockCurrentTimeProvider);

        when(mockCurrentTimeProvider.getCurrentTime()).thenReturn(currentTime);
    }

    @Test
    public void savePairing_createsAPairingHistory_andPersistsIt() {
        Person p1 = new Person("John");
        p1.setId(1L);
        Person p2 = new Person("Mary");
        p2.setId(2L);

        PairingBoard pairingBoard = new PairingBoard("The Pairing Board", Arrays.asList(p1, p2));
        pairingBoard.setId(1L);
        List<PairingBoard> pairingBoards = Collections.singletonList(pairingBoard);

        Project project = new Project("One", "onepass", pairingBoards, new ArrayList<>());

        when(mockProjectRepository.findOne(anyLong())).thenReturn(project);

        pairingService.savePairing(7L);

        PairingHistory expectedPairingHistory = new PairingHistory(project, p1, p2, currentTime, "The Pairing Board");

        verify(mockProjectRepository).findOne(7L);
        verify(mockPairingHistoryRepository).save(eq(expectedPairingHistory));
    }

    @Test
    public void savePairing_createsMultiplePairingHistoriesWithIncrementedGroupIds_whenThereAreMoreThanOnePairingBoards() {
        Person p1 = new Person("John");
        p1.setId(1L);
        Person p2 = new Person("Mary");
        p2.setId(2L);
        Person p3 = new Person("Steve");
        p3.setId(3L);
        Person p4 = new Person("Tammy");
        p4.setId(4L);

        PairingBoard pairingBoard1 = new PairingBoard("The Pairing Board", Arrays.asList(p1, p2));
        pairingBoard1.setId(1L);
        PairingBoard pairingBoard2 = new PairingBoard("The Second Pairing Board", Arrays.asList(p3, p4));
        pairingBoard2.setId(2L);

        List<PairingBoard> pairingBoards = Arrays.asList(pairingBoard1, pairingBoard2);

        Project project = new Project("One", "onepass", pairingBoards, new ArrayList<>());

        when(mockProjectRepository.findOne(anyLong())).thenReturn(project);

        pairingService.savePairing(7L);

        PairingHistory expectedPairingHistory1 = new PairingHistory(project, p1, p2, currentTime, "The Pairing Board");
        PairingHistory expectedPairingHistory2 = new PairingHistory(project, p3, p4, currentTime, "The Second Pairing Board");

        verify(mockProjectRepository).findOne(7L);
        verify(mockPairingHistoryRepository).save(eq(expectedPairingHistory1));
        verify(mockPairingHistoryRepository).save(eq(expectedPairingHistory2));
    }

    @Test
    public void savePairing_createsMultiplePairingHistoriesWithSameGroupId_whenAPairingBoardHasMoreThanTwoPeople() {
        Person p1 = new Person("John");
        p1.setId(1L);
        Person p2 = new Person("Mary");
        p2.setId(2L);
        Person p3 = new Person("Steve");
        p3.setId(3L);

        PairingBoard pairingBoard = new PairingBoard("The Pairing Board", Arrays.asList(p1, p2, p3));
        pairingBoard.setId(1L);

        List<PairingBoard> pairingBoards = Collections.singletonList(pairingBoard);

        Project project = new Project("One", "onepass", pairingBoards, new ArrayList<>());

        when(mockProjectRepository.findOne(anyLong())).thenReturn(project);

        pairingService.savePairing(7L);

        PairingHistory expectedPairingHistory1 = new PairingHistory(project, p1, p2, currentTime, "The Pairing Board");
        PairingHistory expectedPairingHistory2 = new PairingHistory(project, p1, p3, currentTime, "The Pairing Board");
        PairingHistory expectedPairingHistory3 = new PairingHistory(project, p2, p3, currentTime, "The Pairing Board");

        verify(mockProjectRepository).findOne(7L);
        verify(mockPairingHistoryRepository).save(eq(expectedPairingHistory1));
        verify(mockPairingHistoryRepository).save(eq(expectedPairingHistory2));
        verify(mockPairingHistoryRepository).save(eq(expectedPairingHistory3));
    }

    @Test
    public void getRecommendation_getsTheProjectAndItsPairingHistory_andCallsTheRecommendationService() {
        Project project = new Project("One", "onepass", new ArrayList<>(), new ArrayList<>());
        PairingHistory pairingHistory = new PairingHistory();
        List<PairingHistory> pairingHistories = Collections.singletonList(pairingHistory);

        when(mockProjectRepository.findOne(anyLong())).thenReturn(project);
        when(mockPairingHistoryRepository.findByProject(any(Project.class))).thenReturn(pairingHistories);

        pairingService.getRecommendation(77L);

        verify(mockProjectRepository).findOne(77L);
        verify(mockPairingHistoryRepository).findByProject(project);
        verify(mockRecommendationService).get(project, pairingHistories);
    }

    @Test
    public void getRecommendation_persistsTheResultFromTheRecommendationService_andReturnsTheProject() {
        Project project = new Project("One", "onepass", new ArrayList<>(), new ArrayList<>());
        PairingHistory pairingHistory = new PairingHistory();
        List<PairingHistory> pairingHistories = Collections.singletonList(pairingHistory);

        Project recommendedProject = new Project("One", "onepass", new ArrayList<>(), new ArrayList<>());

        when(mockProjectRepository.findOne(anyLong())).thenReturn(project);
        when(mockPairingHistoryRepository.findByProject(any(Project.class))).thenReturn(pairingHistories);
        when(mockRecommendationService.get(any(Project.class), anyListOf(PairingHistory.class))).thenReturn(recommendedProject);

        Project returnedProject = pairingService.getRecommendation(77L);

        assertThat(returnedProject, equalTo(recommendedProject));

        verify(mockProjectRepository).save(recommendedProject);
    }
}