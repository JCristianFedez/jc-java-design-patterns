package com.cristian.design.patterns;

import com.cristian.design.patterns.system.ArrayTransposeMasterWorker;
import com.cristian.design.patterns.system.MasterWorker;
import com.cristian.design.patterns.system.systemmaster.ArrayTransposeMaster;
import com.cristian.design.patterns.system.systemmaster.Master;
import com.cristian.design.patterns.system.systemworkers.ArrayTransposeWorker;
import com.cristian.design.patterns.system.systemworkers.Worker;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>The <b><em>Master-Worker</em></b> pattern is used when the problem at hand can be solved by
 * dividing into multiple parts which need to go through the same computation and may need to be aggregated to get final result. Parallel
 * processing is performed using a system consisting of a master and some number of workers, where a master divides the work among the
 * workers, gets the result back from them and assimilates all the results to give final result. The only communication is between the
 * master and the worker - none of the workers communicate among one another and the user only communicates with the master to get required
 * job done.</p>
 * <p>In our example, we have generic abstract classes {@link MasterWorker}, {@link Master} and
 * {@link Worker} which have to be extended by the classes which will perform the specific job at hand (in this case finding transpose of
 * matrix, done by {@link ArrayTransposeMasterWorker}, {@link ArrayTransposeMaster} and {@link ArrayTransposeWorker}). The Master class
 * divides the work into parts to be given to the workers, collects the results from the workers and aggregates it when all workers have
 * responded before returning the solution. The Worker class extends the Thread class to enable parallel processing, and does the work once
 * the data has been received from the Master. The MasterWorker contains a reference to the Master class, gets the input from the
 * MasterWorkerDemo and passes it on to the Master. These 3 classes define the system which computes the result. We also have 2 abstract
 * classes {@link Input} and {@link Result}, which contain the input data and result data respectively. The Input class also has an abstract
 * method divideData which defines how the data is to be divided into segments. These classes are extended by {@link ArrayInput} and
 * {@link ArrayResult}.</p>
 */
@Slf4j
public class MasterWorkerDemo {

  /**
   * Program entry point.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    final ArrayTransposeMasterWorker mw = new ArrayTransposeMasterWorker();
    final int rows = 10;
    final int columns = 20;
    final int[][] inputMatrix = ArrayUtilityMethods.createRandomIntMatrix(rows, columns);
    final ArrayInput input = new ArrayInput(inputMatrix);
    final ArrayResult result = (ArrayResult) mw.getResult(input);
    if (result != null) {
      ArrayUtilityMethods.printMatrix(inputMatrix);
      ArrayUtilityMethods.printMatrix(result.data);
    } else {
      LOGGER.info("Please enter non-zero input");
    }
  }

}
