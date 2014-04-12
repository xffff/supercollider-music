/************************************/
/* pp           			       	*/
/*	SYNTHS		     				*/
/************************************/

~load_synths = fork{
	s.waitForBoot(
		SynthDef(\dryaudio, { | amp = 1, dryout = 0, fxout = 1, dur = inf, free_trig = 0 |
			var sound, env;
			env = FreeSelf.kr(free_trig);
			sound = SoundIn.ar([0,1]); // in from sampler ---- 10-11 FOR IN SPDIF 1-2 FROM SOUNDFLOWER
			Out.ar(fxout, sound); // to fx
			Out.ar(dryout, sound);
		}).add;

		SynthDef(\hw_reverb, { | amp = 1, in = 0, out = 0, free_trig = 0, dur = inf, xfade = 0.5 |
			var sound, revsound, env;
			env = FreeSelf.kr(free_trig);
			sound = In.ar(in,2);
			Out.ar(8, sound); // to reverb
			revsound = SoundIn.ar([8,9]); // from reverb
			sound = sound + (revsound * xfade);
			ReplaceOut.ar(out,sound*amp);
		}).add;

		SynthDef(\panner, { | amp = 1, out = 0, in = 0, dur = 10, panfreq = 1, panmul = 1, panadd = 0 |
			var sound, env;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			sound = In.ar(in,1);
			//sound = SinOsc.ar(440) * 0.2;
			sound = PanAz.ar(~numchans, sound, SinOsc.kr(panfreq,mul:panmul,add:panadd));
			ReplaceOut.ar(out,sound*env);
		}).add;

		SynthDef(\limiter, { | amp = 0.5, dryin = 0, fxin = 1, panin = 2, out = 0, free_trig = 0 |
			var sound, pansound, fxsound, drysound, env;
			env = FreeSelf.kr(free_trig);	// might be worth making a gui & faders for this
			drysound = In.ar(dryin, 2);
			drysound = drysound;
			fxsound = In.ar(fxin, 2);
			fxsound = fxsound * 0.5;
			pansound = In.ar(panin, ~numchans);

			ReplaceOut.ar(out,fxsound*amp*0.3); // 16 - 17; 16
			Out.ar(out+(~numchans*2),pansound*amp*0.3);	// 20 - 21; 16+(~numchans*2)
			ReplaceOut.ar(out+~numchans,drysound*0.4); // 18 - 19; 16+~numchans
		}).add;

		// simple ringmod
		SynthDef(\ringmodulator, { | amp = 0.5, dur = 1, pan = 0, freq = 56, in = 0, out = 0 |
			var sound, env;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			// sound = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;	// convert to mono
			sound = In.ar(in,2);
			sound = (sound ring1: FSinOsc.ar(freq));
			//sound = PanAz.ar(5,sound,pan);
			sound = sound*env;
			//4.do({ sound = AllpassC.ar(sound, 0.05, 0.05.rand, 4) });
			Out.ar(out,sound)
		}).add;

		// shift_verb_section reworked as a synthdef

		SynthDef(\shift_verb, { | amp = 1, dur = 1, pan = 0, in = 0, out = 0 |
			var trig, input, r, freq, sound, env, delaylines, reverb, shift_sound;

			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			//input = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			input = In.ar(in,2);

			//this is the shift section
			trig = Dust.kr(5);
			r = Lag.kr(TRand.kr(3.5441, 1.75, trig), TRand.kr(1/5,5,trig));	// stable range
			freq = QuadC.ar((96000/8), r.neg, r, 0, 0.1, 11, 12);
			freq = (freq * 0.057762265).exp.min(4.0).max(1.0);
			shift_sound = PitchShift.ar(input, 0.1, freq, 0.0001, 0.0001);

			//reverb section
			delaylines = (shift_sound + input)/4; // charge delay line
			reverb = Mix.ar(Array.fill(6,{ |i|
				9.do{ |j|
					delaylines = DelayC.ar(delaylines, 5, Rand(0.1,5.0/j), 1 / (2**i), delaylines*0.5); // feedback modified....
				};
				delaylines = DelayC.ar(delaylines, 5, ExpRand(0.1,5.0/i), 1 / (2**i), delaylines*0.5);
			}));
			sound = input + Limiter.ar(reverb,0.75,0.01);
			// pan a bit (stereo)
			//sound = PanAz.ar(5,sound,pan)*env;

			Out.ar(out,sound);
		}).add;

		SynthDef(\delay_verb, { | amp = 1, dur = 1, pan = 0, in = 0, out = 0, fb = 0.5 |
			var trig, input, r, freq, sound, env, delaylines, reverb;

			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			input = In.ar(in,2);

			//reverb section
			delaylines = input; // charge delay line
			reverb = Mix.ar(Array.fill(6,{ |i|
				9.do{ |j|
					delaylines = DelayC.ar(delaylines, 5, Rand(0.1,5.0/j), 1 / (2**i), delaylines*fb); // feedback modified....
				};
				delaylines = DelayC.ar(delaylines, 5, ExpRand(0.1,5.0/i), 1 / (2**i), delaylines*fb);
			}));
			sound = Limiter.ar(reverb*0.5,0.75,0.01);
			Out.ar(out,sound*env);
		}).add;

		SynthDef(\shift, { | amp = 1, dur = 10, degree = -12, in = 0, out = 0 |
			var sound, env;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);

			// in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			sound = In.ar(in,2);
			sound = PitchShift.ar(sound, 0.1, (degree * 0.057762265).exp, 0, 0.0001);
			//sound = PanAz.ar(5,sound,SinOsc.kr(rrand(0.1,1.0)));
			Out.ar(out,sound*env);
		}).add;

		SynthDef(\fshift, { | amp = 1, dur = 10, freq = 0, lagdur = 0, in = 0, out = 0 |
			var sound, env;

			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			//in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			sound = In.ar(in,2);
			sound = FreqShift.ar(sound, Lag.kr(freq,lagdur)); /// primes needs to be instantiated first
			//sound = PanAz.ar(5,sound,SinOsc.kr(rrand(0.1,1.0)));
			Out.ar(out,sound*env);
		}).add;



		SynthDef(\quick_delay_s, { | delaytime = 0.333, amp = 1, dur = 2, pan = 0, degree = 0, in = 0, out = 0,freq=0.1,mul=0 |
			var sound, env, ratio;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			ratio = (degree * 0.057762265).exp;
			// sound = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			sound = In.ar(in,2);
			sound = DelayC.ar(sound,11,delaytime*LFNoise0.kr(freq,add:1,mul:mul));
			sound = PitchShift.ar(sound, 0.01, ratio, 0, 0.0001);
			//sound = PanAz.ar(5,sound,pan);
			Out.ar(out,sound*env);
		}).add;

		// delay shifting grain thing
		SynthDef(\delay_shift, { | amp = 1, inamp = 1, dtime_array = #[1, 3, 5, 7, 9, 11, 13], distortion = 0, feedback = 0, lag = 10, dur = 1, in = 0, out = 0 |
			var input, times, sound, env, dist_k;

			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			//input = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			input = In.ar(in,2);
			sound = Mix.ar(Array.fill(10,{
				var l_sound, trigger, freq, octave, dtime;
				dtime = [1,3,5,7,11,13,17,19].choose;
				octave = [-2,-1,1,2].choose;
				trigger = Impulse.kr(dtime_array.choose);
				freq = Drand(Scale.minor.degrees,inf);	// perhaps choose a different scale
				freq = Demand.kr(trigger, 0, freq);
				freq = freq * octave;
				freq = (freq * 0.057762265).exp;
				l_sound = DelayC.ar(input, 20, dtime);// + InFeedback.ar(20, 1); //feedback in... VERY CAREFUL.
				l_sound = PitchShift.ar(l_sound, 0.5, freq, 0, 0.004);
				// OffsetOut.ar(20,l_sound*feedback); // feedback out
				PanAz.ar(2,l_sound,SinOsc.kr(rrand(0.1,0.2)));
			}));
			//distort
			distortion = Lag.kr(distortion, lag); // set this lag to the same number as the routine!
			dist_k = 2*distortion/(1-distortion);
			sound = (1+distortion) * sound / (1+(dist_k * sound.abs));
			sound = Limiter.ar(sound,1.0,0.01);
			Out.ar(out,sound*env);
		}).add;

		SynthDef(\delay_shift_2, { | amp = 1, inamp = 1, dtime_array = #[1, 3, 5, 7, 9, 11, 13], distortion = 0, feedback = 0, lag = 10, dur = 1, in = 0, out = 0 |
			var input, times, sound, env, dist_k;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			//input = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			input = In.ar(in,2);
			sound = Mix.ar(Array.fill(15,{
				var l_sound, trigger, freq, octave, dtime;
				dtime = [1,3,5,7,11,13,17,19].choose;
				octave = [-2,-1,1,2].choose;
				trigger = Impulse.kr(dtime_array.choose);
				freq = Drand(Scale.minor.degrees,inf);	// perhaps choose a different scale
				freq = Demand.kr(trigger, 0, freq);
				freq = freq * octave;
				freq = (freq * 0.057762265).exp;
				l_sound = DelayC.ar(input, 20, dtime);// + InFeedback.ar(20, 1); //feedback in... VERY CAREFUL.
				l_sound = PitchShift.ar(l_sound, 0.5, freq, 0, 0.004);
				// OffsetOut.ar(20,l_sound*feedback); // feedback out
				PanAz.ar(2,l_sound,SinOsc.kr(rrand(0.1,0.2)));
			}));
			//distort
			distortion = Lag.kr(distortion, lag); // set this lag to the same number as the routine!
			dist_k = 2*distortion/(1-distortion);
			sound = (1+distortion) * sound / (1+(dist_k * sound.abs));
			sound = Limiter.ar(sound,1.0,0.01);
			Out.ar(out,sound*env);
		}).add;

		SynthDef(\quick_delay, { | delaytime = 0.333, amp = 1, dur = 2, pan = 0, degree = 0, in = 0, out = 0 |
			var sound, env, ratio;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			ratio = (degree * 0.057762265).exp;
			// sound = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			sound = In.ar(in,2);
			sound = DelayC.ar(sound,11,delaytime);
			sound = PitchShift.ar(sound, 0.01, ratio, 0, 0.004);
			//sound = PanAz.ar(5,sound,pan);
			Out.ar(out,sound*env);
		}).add;

		SynthDef(\tank, { | dur = 10, amp = 1, in = 0, out = 0 |
			var local, env;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			//	in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			local = In.ar(in,2);
			4.do { local = AllpassC.ar(in, 0.03, {Rand(0.005,0.02)}.dup, 1); };
			local = LocalIn.ar(2) * 0.98;
			local = OnePole.ar(local, 0.5);
			local = AllpassC.ar(local, 0.05, Rand(0.01,0.05), 2);
			local = DelayC.ar(local, 0.3, Rand(0.79,0.26));
			local = AllpassC.ar(local, 0.05, Rand(0.03,0.15), 2);
			local = LeakDC.ar(local);
			local = local + in;
			LocalOut.ar(local*0.5);
			//local = PanAz.ar(5,local,SinOsc.kr(rrand(0.1,0.25)));
			Out.ar(out,local*env);
		}).add;

		SynthDef(\tank_dist, { |dur = 10, amp = 1, distortion = 0, in = 0, out = 0 |
			var local, env, dist_k;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);

			// in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
			local = In.ar(in,2);
			4.do { local = AllpassC.ar(in, 0.03, {Rand(0.005,0.02)}.dup, 1); };
			local = LocalIn.ar(2) * 0.98;
			local = OnePole.ar(local, 0.5);
			local = AllpassC.ar(local, 0.05, Rand(0.01,0.05), 2);
			local = DelayC.ar(local, 0.3, Rand(0.79,0.26));
			local = AllpassC.ar(local, 0.05, Rand(0.03,0.15), 2);
			local = LeakDC.ar(local);
			local = local + in;
			LocalOut.ar(local*0.5);

			dist_k = 2*distortion/(1-distortion);

			local = (1+distortion) * local / (1+(dist_k * local.abs));
			local = Limiter.ar(local,1.0,0.01);
			//local = PanAz.ar(5,local,SinOsc.kr(rrand(0.1,0.25)));

			Out.ar(out,local*env);
		}).add;

		SynthDef(\rec_buf, { | dur = 10, amp = 1, bufnum = 0, in = 0 |
			var sound, env;
			//sound = SoundIn.ar([10,11]);
			sound = In.ar(in,2);

			env = Line.kr(0,1,dur,doneAction:2);
			RecordBuf.ar(sound, bufnum, doneAction: 2, loop: 0);
		}).add;

		SynthDef(\play_buf, { | dur = 10, amp = 1, rate = 1, bufnum = 0, pan = 0, out = 0 |
			var sound, env;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			sound = PlayBuf.ar(2, bufnum, rate, doneAction: 2);
			//DetectSilence.ar(sound, doneAction: 2); // just in case I accidentally record a tiny bit into the 71 second buffer and overshoot
			//sound = PanAz.ar(5,sound,pan); // stereo file may break this
			Out.ar(out,sound*env);
		}).add;

		SynthDef(\play_buf_d, { | dur = 10, amp = 1, degree = 0, bufnum = 0, pan = 0, out = 0 |
			var sound, env,rate;
			env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
			rate = (degree * 0.057762265).exp;
			sound = PlayBuf.ar(2, bufnum, rate, doneAction: 2);
			//DetectSilence.ar(sound, doneAction: 2); // just in case I accidentally record a tiny bit into the 71 second buffer and overshoot
			//sound = PanAz.ar(5,sound,pan); // stereo file may break this
			Out.ar(out,sound*env);
		}).add;

		s.sync;
		postln("Synths Loaded...");

	)
};
