package top.dddclub.payroll.payrollcontext.domain.hourlyemployee;

import org.junit.Test;
import top.dddclub.payroll.payrollcontext.domain.Currency;
import top.dddclub.payroll.payrollcontext.domain.Salary;
import top.dddclub.payroll.payrollcontext.domain.Period;
import top.dddclub.payroll.payrollcontext.domain.Payroll;
import top.dddclub.payroll.fixture.EmployeeFixture;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static top.dddclub.payroll.fixture.EmployeeFixture.hourlyEmployeeOf;

public class HourlyEmployeeTest {

    private final Period settlementPeriod = new Period(LocalDate.of(2019, 9, 2), LocalDate.of(2019, 9, 6));
    private final String employeeId = "emp200901011111";

    @Test
    public void should_calculate_payroll_by_work_hours_in_a_week() {
        //given
        HourlyEmployee hourlyEmployee = hourlyEmployeeOf(employeeId, 8, 8, 8, 8, 8);

        //when
        Payroll payroll = hourlyEmployee.payroll(settlementPeriod);

        //then
        assertThat(payroll).isNotNull();
        assertThat(payroll.employeId().value()).isEqualTo(employeeId);
        assertThat(payroll.beginDate()).isEqualTo(LocalDate.of(2019, 9, 2));
        assertThat(payroll.endDate()).isEqualTo(LocalDate.of(2019, 9, 6));
        assertThat(payroll.amount()).isEqualTo(Salary.of(4000.00, Currency.RMB));
    }

    @Test
    public void should_calculate_payroll_by_work_hours_with_overtime_in_a_week() {
        //given
        HourlyEmployee hourlyEmployee = hourlyEmployeeOf(employeeId, 9, 7, 10, 10, 8);

        //when
        Payroll payroll = hourlyEmployee.payroll(settlementPeriod);

        //then
        assertThat(payroll).isNotNull();
        assertThat(payroll.employeId().value()).isEqualTo(employeeId);
        assertThat(payroll.beginDate()).isEqualTo(LocalDate.of(2019, 9, 2));
        assertThat(payroll.endDate()).isEqualTo(LocalDate.of(2019, 9, 6));
        assertThat(payroll.amount()).isEqualTo(Salary.of(4650.00, Currency.RMB));
    }

    @Test
    public void should_be_0_given_no_any_timecard() {
        //given
        HourlyEmployee hourlyEmployee = EmployeeFixture.hourlyEmployeeOf(employeeId, new ArrayList<>());

        //when
        Payroll payroll = hourlyEmployee.payroll(settlementPeriod);

        //then
        assertThat(payroll).isNotNull();
        assertThat(payroll.employeId().value()).isEqualTo(employeeId);
        assertThat(payroll.amount()).isEqualTo(Salary.of(0.00, Currency.RMB));
    }

    @Test
    public void should_be_0_given_null_timecard() {
        //given
        HourlyEmployee hourlyEmployee = EmployeeFixture.hourlyEmployeeOf(employeeId, null);

        //when
        Payroll payroll = hourlyEmployee.payroll(settlementPeriod);

        //then
        assertThat(payroll).isNotNull();
        assertThat(payroll.employeId().value()).isEqualTo(employeeId);
        assertThat(payroll.amount()).isEqualTo(Salary.zero());
    }
}
