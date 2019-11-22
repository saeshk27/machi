package com.genesyslab.machi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.genesyslab.machi.domain.SprintMetrics;

@Component
public class ScorerUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScorerUtil.class);

	@Value("${sprint.rating.cutoff.min}")
	private int minCutoff;
	@Value("${sprint.rating.cutoff.max}")
	private int maxCutoff;

	private int plannedTickets;
	private int completedTickets;
	private int wipTickets;
	private int openTickets;
	private int blockedTickets;
	private int ticketsWorked;
	private int unplannedProdTickets;
	private int unplannedOtherEnvTickets;
	private int unplannedDefectTickets;

	private int totalWeightage;
	private int userScorePercentage;

	private int plannedWorkingHours;
	private int actualWorkingHours;

	private double userScore;
	private double ticketScore;
	private double attendanceScore;
	private double complexityScore;
	private double valueAddScore;

	public void calculateUserScore(SprintMetrics metrics) {
		this.plannedTickets = metrics.getPlannedTickets();
		this.completedTickets = metrics.getCompletedTickets();
		this.wipTickets = metrics.getWipTickets();
		this.openTickets = metrics.getOpenTickets();
		this.blockedTickets = metrics.getBlockedTickets();

		// Unable to fetch these records because of not integrating with JIRA
		// this.unplannedProdTickets;
		// this.unplannedOtherEnvTickets;
		// this.unplannedDefectTickets;
		// this.scheduledWorkingHours;
		// this.plannedWorkingHours;
		// this.actualWorkingHours;

		ticketsWorked = completedTickets + wipTickets + blockedTickets;
		totalWeightage = plannedTickets;

		ticketScore = getTicketScore();
		attendanceScore = getAttendanceScore();
		complexityScore = getComplexityScore();
		valueAddScore = getUnplannedScore();

		userScore = ticketScore + attendanceScore + complexityScore + valueAddScore;

		userScorePercentage = (int) (userScore * 100 / totalWeightage);

		LOGGER.info(
				"calculateUserScore() | Calculated scores | ticketScore: {}, attendanceScore: {}, complexityScore: {}, valueAddScore:{}, userScore:{}, userScorePercentage:{} ",
				ticketScore, attendanceScore, complexityScore, valueAddScore, userScore, userScorePercentage);
	}

	public boolean isBurdened() {
		if (userScorePercentage > maxCutoff) {
			return true;
		}
		return false;
	}

	public boolean isLagging() {
		if (userScorePercentage < minCutoff) {
			return true;
		}
		return false;
	}

	private double getTicketScore() {
		double mark = completedTickets + (0.5 * wipTickets) + (0.75 * blockedTickets);
		if (hasWorkedLesserThanPlanned()) {
			mark -= (0.5 * openTickets);
		}
		return mark;
	}

	private double getAttendanceScore() {
		double mark = 0;
		if (hasWorkedLesserThanPlanned()) {
			if (plannedWorkingHours > actualWorkingHours) {
				mark += 1;
			} else if (plannedWorkingHours == actualWorkingHours) {
				mark -= 0.5;
			} else {
				mark -= 1.5;
			}
		} else if (plannedTickets == ticketsWorked) {
			if (plannedWorkingHours < actualWorkingHours) {
				mark += 2;
			} else if (plannedWorkingHours == actualWorkingHours) {
				mark += 0;
			} else {
				mark -= 3;
			}
		} else {
			if (plannedWorkingHours > actualWorkingHours) {
				mark += 3;
			} else if (plannedWorkingHours == actualWorkingHours) {
				mark += 1.5;
			} else {
				mark -= 1;
			}
		}
		return mark;
	}

	private double getUnplannedScore() {
		double mark = 0;
		if (hasWorkedLesserThanPlanned()) {
			if (unplannedProdTickets > 0) {
				mark += 2;
			}
			if (unplannedOtherEnvTickets > 0) {
				mark += 1;
			}
			if (unplannedDefectTickets > 0) {
				mark -= 3.5;
			}
		} else if (plannedTickets == ticketsWorked) {
			if (unplannedProdTickets > 0) {
				mark += 2.75;
			}
			if (unplannedOtherEnvTickets > 0) {
				mark += 1.5;
			}
			if (unplannedDefectTickets > 0) {
				mark -= 2;
			}
		} else {
			if (unplannedProdTickets > 0) {
				mark += 4;
			}
			if (unplannedOtherEnvTickets > 0) {
				mark += 2.25;
			}
			if (unplannedDefectTickets > 0) {
				mark -= 1;
			}
		}
		return mark;
	}

	/**
	 * Unable to implement because of JIRA integration
	 * 
	 * @return
	 */
	private double getComplexityScore() {
		double mark = 0;
		return mark;
	}

	private boolean hasWorkedLesserThanPlanned() {
		if (plannedTickets < ticketsWorked) {
			return false;
		}
		return true;
	}
}
