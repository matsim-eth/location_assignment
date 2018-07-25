package ch.ethz.matsim.location_assignment.matsim.examples.sbb;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.ethz.matsim.location_assignment.matsim.examples.zurich.ZurichDistanceSamplerFactory;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.core.population.PopulationUtils;

import ch.ethz.matsim.location_assignment.algorithms.DistanceSampler;
import ch.ethz.matsim.location_assignment.algorithms.discretizer.Discretizer;
import ch.ethz.matsim.location_assignment.matsim.MATSimAssignmentProblem;
import ch.ethz.matsim.location_assignment.matsim.discretizer.FacilityTypeDiscretizerFactory;
import ch.ethz.matsim.location_assignment.matsim.setup.MATSimDiscretizationThresholdProvider;
import ch.ethz.matsim.location_assignment.matsim.setup.MATSimDiscretizerProvider;
import ch.ethz.matsim.location_assignment.matsim.setup.MATSimDistanceSamplerProvider;

public class SBBProblemProvider
        implements MATSimDistanceSamplerProvider, MATSimDiscretizationThresholdProvider, MATSimDiscretizerProvider {
    final private ZurichDistanceSamplerFactory distanceSamplerFactory;
    final private FacilityTypeDiscretizerFactory discretizerFactory;
    final private Map<String, Double> discretizationThresholds;

    public SBBProblemProvider(ZurichDistanceSamplerFactory distanceSamplerFactory,
                                 FacilityTypeDiscretizerFactory discretizerFactory, Map<String, Double> discretizationThresholds) {
        this.distanceSamplerFactory = distanceSamplerFactory;
        this.discretizerFactory = discretizerFactory;
        this.discretizationThresholds = discretizationThresholds;
    }

    @Override
    public List<Discretizer> getDiscretizers(MATSimAssignmentProblem problem) {
        return problem.getChainActivities().stream().map(discretizerFactory::createDiscretizer)
                .collect(Collectors.toList());
    }

    @Override
    public List<Double> getDiscretizationThresholds(MATSimAssignmentProblem problem) {
        return problem.getAllLegs().stream().map(Leg::getMode).map(discretizationThresholds::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistanceSampler> getDistanceSamplers(MATSimAssignmentProblem problem) {
        return problem.getAllLegs().stream().map(leg -> {
            double duration = PopulationUtils.getNextActivity(problem.getPlan(), leg).getStartTime()
                    - PopulationUtils.getPreviousActivity(problem.getPlan(), leg).getEndTime();
            return distanceSamplerFactory.createDistanceSampler(leg.getMode(), duration);
        }).collect(Collectors.toList());
    }
}
