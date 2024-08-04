# Self-adaptive polynomial mutation in NSGA-II

This repository contains the supplementary material for the paper:

Carles-Bou, J.L., Galán, S.F. Self-adaptive polynomial mutation in NSGA-II. Soft Comput 27, 17711–17727 (2023). 
https://doi.org/10.1007/s00500-023-09049-0

# Paper Abstract
Evolutionary multi-objective optimization is a field that has experienced a rapid growth in the last two decades. Although animportant number of new multi-objective evolutionary algorithms have been designed and implemented by the scientific com-munity, the popular Non-Dominated Sorting Genetic Algorithm (NSGA-II) remains as a widely used baseline for algorithmperformance comparison purposes and applied to different engineering problems. Since every evolutionary algorithm needsseveral parameters to be set up in order to operate, parameter control constitutes a crucial task for obtaining an effective andefficient performance in its execution. However, despite the advancements in parameter control for evolutionary algorithms,NSGA-II has been mainly used in the literature with fine-tuned static parameters. This paper introduces a novel and compu-tationally lightweight self-adaptation mechanism for controlling the distribution index parameter of the polynomial mutationoperator usually employed by NSGA-II in particular and by multi-objective evolutionary algorithms in general. Additionally,the classical NSGA-II using polynomial mutation with a static distribution index is compared with this new version utilizinga self-adapted parameter. The experiments carried out over twenty-five benchmark problems show that the proposed modifiedNSGA-II with a self-adaptive mutator outperforms its static counterpart in more than 75% of the problems using three qualitymetrics (hypervolume, generalized spread, and modified inverted generational distance).

## License

This work is licensed under Creative Commons Zero v1.0 Universal (or any later version).
