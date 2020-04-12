package spinal.sim

import java.math.BigInteger
import spinal.sim.vpi._
import scala.collection.JavaConverters._

import scala.collection.mutable.ArrayBuffer
import sys.process._

object Bench {
  def apply(testbench: => Unit): Unit = {
    var retry = 0
    while (retry < 2) {
      val startAt = System.nanoTime
      testbench
      val endAt = System.nanoTime
      System.out.println((endAt - startAt) * 1e-6 + " ms")
      retry += 1;
    }
  }
}
//
//object Test2 {
//  def main(args: Array[String]): Unit = {
//    val config = new BackendConfig()
//    config.rtlSourcesPaths += "sim/TopLevel.v"
//    config.toplevelName = "TopLevel"
//    config.workspacePath = "yolo"
//
//    val vConfig = new VerilatorBackendConfig
////    vConfig.signals ++= List("io_a", "io_b", "io_result").map(name => new VerilatorSignal(List(name),8))
//    val backend = new VerilatorBackend(config,vConfig)
//    val wrapper = backend.instanciate()
//
////    Bench {
////      val handle = wrapper.wrapperNewHandle()
////      var counter = 0
////
////      var idx = 1000000
////      while (idx != 0) {
////        idx -= 1
////        wrapper.wrapperSetCData(handle, 0, 3)
////        wrapper.wrapperSetCData(handle, 1, 6)
////        wrapper.wrapperSetCData(handle, 0, 3)
////        wrapper.wrapperSetCData(handle, 1, 6)
////        wrapper.wrapperSetCData(handle, 0, 3)
////        wrapper.wrapperSetCData(handle, 1, 6)
////        wrapper.wrapperSetCData(handle, 0, 3)
////        wrapper.wrapperSetCData(handle, 1, 6)
////        wrapper.wrapperSetCData(handle, 0, 3)
////        wrapper.wrapperSetCData(handle, 1, 6)
////        wrapper.wrapperEval(handle)
////        counter += wrapper.wrapperGetCData(handle, 2)
////      }
////      println(counter)
////    }
//  }
//}

object PlayGhdl extends App{
  val config = new GhdlBackendConfig()
  config.rtlSourcesPaths += "adder.vhd"
  config.toplevelName = "adder"
  config.pluginsPath = "simulation_plugins"
  config.workspacePath = "yolo"
  config.workspaceName = "yolo"
  config.wavePath = "test.vcd"
  config.waveFormat = WaveFormat.VCD

  val ghdlbackend = new GhdlBackend(config).instanciate
  println(ghdlbackend.print_signals())
  val nibble1 = ghdlbackend.get_signal_handle("adder.nibble1")
  val nibble2 = ghdlbackend.get_signal_handle("adder.nibble2")
  val sum = ghdlbackend.get_signal_handle("adder.sum")
  ghdlbackend.write32(nibble1, 0)
  ghdlbackend.eval
  ghdlbackend.write32(nibble1, 3)
  ghdlbackend.write32(nibble2, 5)
  println("? = " + ghdlbackend.read32(nibble1).toString)
  ghdlbackend.eval
  println("3 = " + ghdlbackend.read32(nibble1).toString)
  println("3 + 5 = " + ghdlbackend.read32(sum).toString)
  ghdlbackend.write64(nibble1, 4)
  ghdlbackend.write64(nibble2, 1)
  ghdlbackend.sleep(3)
  println("4 + 1 = " + ghdlbackend.read64(sum).toString)
  ghdlbackend.write(nibble1, new VectorInt8(BigInt(2).toByteArray))
  ghdlbackend.write(nibble2, new VectorInt8(BigInt(3).toByteArray))
  ghdlbackend.eval
  println("2 + 3 = " + BigInt(ghdlbackend.read(sum)
                                         .asScala
                                         .toArray
                                         .map{x => x.toByte}).toString)
  ghdlbackend.close
  println("Finished PlayGhdl")
}

object StressGhdl1 extends App{
  val config = new GhdlBackendConfig()
  config.rtlSourcesPaths += "adder.vhd"
  config.toplevelName = "adder"
  config.pluginsPath = "simulation_plugins"
  config.workspacePath = "yolo"
  config.workspaceName = "yolo"
  config.wavePath = "test.vcd"
  config.waveFormat = WaveFormat.VCD

  val ghdlbackend = new GhdlBackend(config).instanciate
  for(i <- 0 to 100000){
    ghdlbackend.eval
    ghdlbackend.eval
    ghdlbackend.eval
    ghdlbackend.eval
    ghdlbackend.sleep(10)
    ghdlbackend.sleep(10)
    ghdlbackend.sleep(10)
    ghdlbackend.sleep(10)
    ghdlbackend.sleep(10)
    ghdlbackend.sleep(10)
    ghdlbackend.eval
    ghdlbackend.sleep(10)
    ghdlbackend.eval
    ghdlbackend.sleep(10)
    ghdlbackend.eval
    ghdlbackend.sleep(10)
    ghdlbackend.eval
    ghdlbackend.sleep(10)
    ghdlbackend.eval
    ghdlbackend.sleep(10)
    ghdlbackend.eval
    ghdlbackend.sleep(10)
    ghdlbackend.eval
    ghdlbackend.eval
  }
  ghdlbackend.close
  println("Finished StressGhdl1")
}

object StressGhdl2 extends App{
  val config = new GhdlBackendConfig()
  config.rtlSourcesPaths += "adder.vhd"
  config.toplevelName = "adder"
  config.pluginsPath = "simulation_plugins"
  config.workspacePath = "yolo"
  config.workspaceName = "yolo"
  config.wavePath = "test.vcd"
  config.waveFormat = WaveFormat.VCD

  val ghdlbackend = new GhdlBackend(config).instanciate
  val nibble1 = ghdlbackend.get_signal_handle("adder.nibble1")
  val nibble2 = ghdlbackend.get_signal_handle("adder.nibble2")
  val sum = ghdlbackend.get_signal_handle("adder.sum")
  for(i <- 0 to 100000){
    ghdlbackend.write32(nibble1, 0)
    ghdlbackend.eval
    ghdlbackend.eval
    ghdlbackend.write32(nibble1, 3)
    ghdlbackend.write32(nibble2, 5)
    ghdlbackend.eval
    println("3 = " + ghdlbackend.read32(nibble1).toString)
    println("3 + 5 = " + ghdlbackend.read32(sum).toString)
    ghdlbackend.write64(nibble1, 4)
    ghdlbackend.write64(nibble2, 1)
    ghdlbackend.sleep(3)
    println("4 + 1 = " + ghdlbackend.read64(sum).toString)
    ghdlbackend.write(nibble1, new VectorInt8(BigInt(2).toByteArray))
    ghdlbackend.write(nibble2, new VectorInt8(BigInt(3).toByteArray))
    ghdlbackend.eval
    println("2 + 3 = " + BigInt(ghdlbackend.read(sum)
                                           .asScala
                                           .toArray
                                           .map{x => x.toByte}).toString)
  }
  ghdlbackend.close
  println("Finished StressGhdl2")
}

//object PlayIVerilog extends App{
//  val config = new IVerilogBackendConfig()
//  config.rtlSourcesPaths += "adder.v"
//  config.toplevelName = "adder"
//  config.pluginsPath = "simulation_plugins"
//  config.workspacePath = "yolo"
//  config.workspaceName = "yolo"
//
//  val iverilogbackend = new IVerilogBackend(config).instanciate
//  val nibble1 = iverilogbackend.get_signal_handle("adder.nibble1")
//  val nibble2 = iverilogbackend.get_signal_handle("adder.nibble2")
//  val sum = iverilogbackend.get_signal_handle("adder.sum")
//
//  iverilogbackend.write32(nibble1, 0)
//  iverilogbackend.eval
//  iverilogbackend.write32(nibble1, 3)
//  iverilogbackend.write32(nibble2, 5)
//  println("? = " + iverilogbackend.read32(nibble1).toString)
//  iverilogbackend.eval
//  println("3 = " + iverilogbackend.read32(nibble1).toString)
//  println("3 + 5 = " + iverilogbackend.read32(sum).toString)
//  iverilogbackend.write64(nibble1, 4)
//  iverilogbackend.write64(nibble2, 1)
//  iverilogbackend.sleep(3)
//  println("4 + 1 = " + iverilogbackend.read64(sum).toString)
//  iverilogbackend.write(nibble1, new VectorInt8(BigInt(2).toByteArray))
//  iverilogbackend.write(nibble2, new VectorInt8(BigInt(3).toByteArray))
//  iverilogbackend.eval
//  println("2 + 3 = " + BigInt(iverilogbackend.read(sum)
//                                             .asScala
//                                             .toArray
//                                             .map{x => x.toByte}).toString)
//  iverilogbackend.close
//  println("Finished PlayIVerilog")
//}

