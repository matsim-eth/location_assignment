package ch.ethz.matsim.location_assignment.matsim.examples.sbb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import ch.ethz.matsim.location_assignment.matsim.examples.zurich.ZurichDistanceSamplerFactory;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.population.io.PopulationWriter;
import org.matsim.core.router.MainModeIdentifier;
import org.matsim.core.router.MainModeIdentifierImpl;
import org.matsim.core.router.StageActivityTypes;
import org.matsim.core.router.StageActivityTypesImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.facilities.MatsimFacilitiesReader;
import org.matsim.pt.PtConstants;

import ch.ethz.matsim.location_assignment.matsim.discretizer.FacilityTypeDiscretizerFactory;
import ch.ethz.matsim.location_assignment.matsim.solver.MATSimAssignmentSolver;
import ch.ethz.matsim.location_assignment.matsim.solver.MATSimAssignmentSolverBuilder;
import ch.ethz.matsim.location_assignment.matsim.utils.LocationAssignmentPlanAdapter;
import org.matsim.utils.objectattributes.ObjectAttributesXmlReader;

public class RunSBBLocationAssignment {
    static public void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // CONFIGURATION

        String facilitiesInputPath = args[0];
        String facilityAttributesInputPath = args[1];
        String populationInputPath = args[2];
        String quantilesPath = args[3];
        String distributionsPath = args[4];
        String outputPath = args[5];
        String statisticsOutputPath = args[6];

        Set<String> relevantActivityTypes = new HashSet<>(
                Arrays.asList("leisure", "work"));

        Map<String, Double> discretizationThresholds = new HashMap<>();
        discretizationThresholds.put("car", 200.0);
        discretizationThresholds.put("ride", 200.0);
        discretizationThresholds.put("pt", 200.0);
        discretizationThresholds.put("bike", 100.0);
        discretizationThresholds.put("walk", 100.0);

        // LOAD POPULATION & FACILITIES

        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

        new MatsimFacilitiesReader(scenario).readFile(facilitiesInputPath);
        new PopulationReader(scenario).readFile(populationInputPath);
        new ObjectAttributesXmlReader(scenario.getActivityFacilities().getFacilityAttributes()).readFile(facilityAttributesInputPath);

        StageActivityTypes stageActivityTypes = new StageActivityTypesImpl(PtConstants.TRANSIT_ACTIVITY_TYPE);
        MainModeIdentifier mainModeIdentifier = new MainModeIdentifierImpl();

        Random random = new Random(0);

        // SET UP DISTANCE SAMPLERS

        FacilityTypeDiscretizerFactory discretizerFactory = new FacilityTypeDiscretizerFactory(relevantActivityTypes);
        discretizerFactory.loadFacilities(scenario.getActivityFacilities());

        ZurichDistanceSamplerFactory distanceSamplerFactory = new ZurichDistanceSamplerFactory(random);
        distanceSamplerFactory.load(quantilesPath, distributionsPath);

        // set up zurich specifics

        OutputStream statisticsStream = new FileOutputStream(statisticsOutputPath);
        SBBStatistics zurichStatistics = new SBBStatistics(scenario.getPopulation().getPersons().size(),
                Optional.of(statisticsStream));
        SBBProblemProvider sbbProblemProvider = new SBBProblemProvider(distanceSamplerFactory,
                discretizerFactory, discretizationThresholds);

        // set up the algorithm

        MATSimAssignmentSolverBuilder builder = new MATSimAssignmentSolverBuilder();

        builder.setVariableActivityTypes(relevantActivityTypes);
        builder.setRandom(random);
        builder.setStageActivityTypes(stageActivityTypes);

        builder.setDiscretizerProvider(sbbProblemProvider);
        builder.setDistanceSamplerProvider(sbbProblemProvider);
        builder.setDiscretizationThresholdProvider(sbbProblemProvider);

        MATSimAssignmentSolver solver = builder.build();

        // loop population

        scenario.getPopulation().getPersons().values().stream().parallel().map(Person::getSelectedPlan)
                .map(solver::createProblems).flatMap(Collection::stream).map(solver::solveProblem)
                .map(zurichStatistics::process).forEach(new LocationAssignmentPlanAdapter());

        new PopulationWriter(scenario.getPopulation()).write(outputPath);
        statisticsStream.close();
    }
}
